package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.PropertyType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import service.FacilityService;
import service.ImageService;
import service.PropertyTypeService;
import service.RoomService;
import service.StructureService;
import utils.I18nUtils;

import com.sun.jersey.api.NotFoundException;

@Path("/propertyTypes/")
@Component
@Scope("prototype")
public class PropertyTypeResource {
	@Autowired
	private PropertyTypeService propertyTypeService = null;
	@Autowired
	private StructureService structureService = null;
	@Autowired
	private RoomService roomService = null;
	@Autowired
	private FacilityService facilityService = null;
	@Autowired
	private ImageService imageService = null;
	@Autowired
	private SolrServer solrServerProperty = null;

	@PostConstruct
	public void init() {
		List<PropertyType> propertys = null;

		propertys = this.getPropertyTypeService().findAll();
		try {
			this.getSolrServerProperty().addBeans(propertys);
			this.getSolrServerProperty().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("search/{start}/{rows}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<PropertyType> search(@PathParam("start") Integer start, @PathParam("rows") Integer rows, @QueryParam("term") String term) {
		List<PropertyType> propertys = null;
		SolrQuery query = null;
		QueryResponse rsp = null;
		SolrDocumentList solrDocumentList = null;
		SolrDocument solrDocument = null;
		PropertyType aProperty = null;
		Integer id;

		if (term.trim().equals("")) {
			term = "*:*";
		}
		query = new SolrQuery();
		query.setQuery(term);
		query.setStart(start);
		query.setRows(rows);

		try {
			rsp = this.getSolrServerProperty().query(query);

		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		propertys = new ArrayList<PropertyType>();
		if (rsp != null) {
			solrDocumentList = rsp.getResults();
			for (int i = 0; i < solrDocumentList.size(); i++) {
				solrDocument = solrDocumentList.get(i);
				id = (Integer) solrDocument.getFieldValue("id");
				// System.out.println("----> "+solrDocument.getFieldValues("text")+" <-----");
				aProperty = this.getPropertyTypeService().findPropertyById(id);

				propertys.add(aProperty);
			}
		}
		return propertys;
	}

	@GET
	@Path("suggest")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<String> suggest(@QueryParam("term") String term) {
		SolrQuery query = null;
		QueryResponse rsp = null;
		List<String> ret = null;
		List<Count> values = null;

		query = new SolrQuery();
		query.setFacet(true);
		query.addFacetField("text");
		term = term.toLowerCase();
		query.setFacetPrefix(term);

		try {
			rsp = this.getSolrServerProperty().query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		ret = new ArrayList<String>();

		if (rsp != null) {
			values = rsp.getFacetField("text").getValues();
			if (values != null) {
				for (Count each : values) {
					// if (each.getCount() > 0) {
					ret.add(each.getName());
					// }
				}
			}
		}
		return ret;
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyType getProperty(@PathParam("id") Integer id) {
		return this.getPropertyTypeService().findPropertyById(id);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyType save(PropertyType property) {

		this.getPropertyTypeService().insertProperty(property);
		try {
			this.getSolrServerProperty().addBean(property);
			this.getSolrServerProperty().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return property;
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public PropertyType update(PropertyType property) {

		try {
			this.getPropertyTypeService().updateProperty(property);
		} catch (Exception ex) {
		}
		try {
			this.getSolrServerProperty().addBean(property);
			this.getSolrServerProperty().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return property;
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Integer delete(@PathParam("id") Integer id) {
		Integer count = 0;

		count = this.getPropertyTypeService().deleteProperty(id);
		if (count == 0) {
			throw new NotFoundException(I18nUtils.getProperty("propertyDeleteErrorAction"));
		}
		try {
			this.getSolrServerProperty().deleteById(id.toString());
			this.getSolrServerProperty().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	public StructureService getStructureService() {
		return structureService;
	}

	public void setStructureService(StructureService structureService) {
		this.structureService = structureService;
	}

	public PropertyTypeService getPropertyTypeService() {
		return propertyTypeService;
	}

	public void setPropertyTypeService(PropertyTypeService propertyTypeService) {
		this.propertyTypeService = propertyTypeService;
	}

	public RoomService getRoomService() {
		return roomService;
	}

	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
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

	public SolrServer getSolrServerProperty() {
		return solrServerProperty;
	}

	public void setSolrServerProperty(SolrServer solrServerProperty) {
		this.solrServerProperty = solrServerProperty;
	}
}