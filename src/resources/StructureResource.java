package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.Structure;
import model.User;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import service.FacilityService;
import service.ImageService;
import service.StructurePropertyTypeService;
import service.StructureService;
import utils.LocandaConstants;

@Path("/structures/")
@Component
@Scope("prototype")
public class StructureResource {
	@Autowired
	private StructureService structureService = null;
	@Autowired
	private FacilityService facilityService = null;
	@Autowired
	private ImageService imageService = null;
	@Autowired
	private SolrServer solrServerStructure = null;
	@Autowired
	private ApplicationContext applicationContext = null;
	@Autowired
	private StructurePropertyTypeService structurePropertyTypeService = null;

	@PostConstruct
	public void init() {
		List<Structure> structures = null;

		structures = this.getStructureService().findAll();
		try {
			this.getSolrServerStructure().addBeans(structures);
			this.getSolrServerStructure().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("search/{start}/{rows}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Structure> search(@PathParam("start") Integer start, @PathParam("rows") Integer rows, @QueryParam("term") String term) {
		List<Structure> structures = null;
		SolrQuery query = null;
		QueryResponse rsp = null;
		SolrDocumentList solrDocumentList = null;
		SolrDocument solrDocument = null;
		Structure aStructure = null;
		Integer id = 0;
		User user = null;

		if (term.trim().equals("")) {
			term = "*:*";
		}
		user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
		term = term + " AND id_user:" + user.getId();
		query = new SolrQuery();
		query.setQuery(term);
		query.setStart(start);
		query.setRows(rows);

		try {
			rsp = this.getSolrServerStructure().query(query);

		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		structures = new ArrayList<Structure>();
		if (rsp != null) {
			solrDocumentList = rsp.getResults();
			for (int i = 0; i < solrDocumentList.size(); i++) {
				solrDocument = solrDocumentList.get(i);
				id = (Integer) solrDocument.getFieldValue("id");
				aStructure = this.getStructureService().findStructureById(id);
				structures.add(aStructure);
			}
		}
		return structures;
	}

	@GET
	@Path("suggest")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<String> suggest(@QueryParam("term") String term) {
		SolrQuery query = null;
		QueryResponse rsp = null;
		List<String> ret = null;
		List<Count> values = null;
		User user = null;

		user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);

		query = new SolrQuery();
		query.setQuery("*:* AND id_user:" + user.getId());
		query.setFacet(true);
		query.addFacetField("text");
		term = term.toLowerCase();
		query.setFacetPrefix(term);

		try {
			rsp = this.getSolrServerStructure().query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		ret = new ArrayList<String>();

		if (rsp != null) {
			values = rsp.getFacetField("text").getValues();
			if (values != null) {
				for (Count each : values) {
					if (each.getCount() > 0) {
						ret.add(each.getName());
					}
				}
			}
		}
		return ret;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Structure getStructure(@PathParam("id") Integer idStructure) {
		Structure ret = null;

		ret = this.getStructureService().findStructureById(idStructure);
		return ret;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Structure save(Structure structure, @QueryParam("id_propertyType") String id_propertyType) {
		// Get current user
		User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
		structure.setId_user(user.getId());

		// if this structure set is enable then other structures will be set
		// disable.
		if (structure.getIsEnable().equals(LocandaConstants.STRUCTURE_IS_ENABLE)) {
			// Get all structure of current user have status isEnable = '1'
			List<Structure> structures = this.getStructureService().findStructureByIdUserByStatus(user.getId(), LocandaConstants.STRUCTURE_IS_ENABLE);
			if (structures != null && !structures.isEmpty()) {
				for (Structure s : structures) {
					// Update status to disable
					s.setIsEnable(LocandaConstants.STRUCTURE_IS_DISABLE);
					this.updateStructure(s);
				}
			}
		}

		this.getStructureService().insertStructure(structure);

		try {
			this.getSolrServerStructure().addBean(structure);
			this.getSolrServerStructure().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		// Get structure id
		Integer idStructureLatest = this.getStructureService().findMaxIdStructure();
		idStructureLatest = idStructureLatest == null ? 0 : idStructureLatest;

		// Create table structure property type.
		for (String id : id_propertyType.split(",")) {
			if (!id.isEmpty() && !"undefined".equals(id_propertyType))
				this.getStructurePropertyTypeService().insert(idStructureLatest, new Integer(id));
		}

		return structure;
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Structure update(Structure structure, @QueryParam("id_propertyType") String id_propertyType) {
		// Get current user
		User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);

		// if this structure set is enable then other structures will be set
		// disable.
		if (structure.getIsEnable().equals(LocandaConstants.STRUCTURE_IS_ENABLE)) {
			// Get all structure of current user have status isEnable = '1'
			List<Structure> structures = this.getStructureService().findStructureByIdUserByStatus(user.getId(), LocandaConstants.STRUCTURE_IS_ENABLE);
			if (structures != null && !structures.isEmpty()) {
				for (Structure s : structures) {
					// Update status to disable
					s.setIsEnable(LocandaConstants.STRUCTURE_IS_DISABLE);
					this.updateStructure(s);
				}
			}

			this.updateStructure(structure);
		} else if (structure.getIsEnable().equals(LocandaConstants.STRUCTURE_IS_DISABLE)) {
			// Get all structure of current user have status isEnable = '1'
			List<Structure> structures = this.getStructureService().findStructureByIdUserByStatus(user.getId(), LocandaConstants.STRUCTURE_IS_ENABLE);
			if (structures == null || structures.isEmpty()) {
				new Exception("You can not update this Structure to disable. Because system must have 1 structure be enabled.");
				return null;
			} else {
				for (Structure s : structures) {
					// If this structure enable in database
					if (s.getId().equals(structure.getId())) {
						new Exception("You can not update this Structure to disable. Because system must have 1 structure be enabled.");
						return null;
					}
				}
				this.updateStructure(structure);
			}
		}

		// Remove old relation
		this.getStructurePropertyTypeService().deleteByIdStructure(structure.getId());

		// Create table structure property type.
		for (String id : id_propertyType.split(",")) {
			if (!id.isEmpty() && !"undefined".equals(id_propertyType))
				this.getStructurePropertyTypeService().insert(structure.getId(), new Integer(id));
		}

		return structure;
	}

	private void updateStructure(Structure s) {
		try {
			this.getStructureService().updateStructure(s);
			this.getSolrServerStructure().addBean(s);
			this.getSolrServerStructure().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception ex) {
		}
	}

	public StructureService getStructureService() {
		return structureService;
	}

	public void setStructureService(StructureService structureService) {
		this.structureService = structureService;
	}

	public FacilityService getFacilityService() {
		return facilityService;
	}

	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}

	public ImageService getImageService() {
		return imageService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public SolrServer getSolrServerStructure() {
		return solrServerStructure;
	}

	public void setSolrServerStructure(SolrServer solrServerStructure) {
		this.solrServerStructure = solrServerStructure;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public StructurePropertyTypeService getStructurePropertyTypeService() {
		return structurePropertyTypeService;
	}

	public void setStructurePropertyTypeService(StructurePropertyTypeService structurePropertyTypeService) {
		this.structurePropertyTypeService = structurePropertyTypeService;
	}
}