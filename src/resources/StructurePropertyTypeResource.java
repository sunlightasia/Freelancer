package resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import service.PropertyTypeService;
import service.StructurePropertyTypeService;
import utils.I18nUtils;

import com.sun.jersey.api.NotFoundException;

@Path("/structurePropertyTypes/")
@Component
@Scope("prototype")
public class StructurePropertyTypeResource {
	@Autowired
	private PropertyTypeService propertyTypeService = null;
	@Autowired
	private StructurePropertyTypeService structurePropertyTypeService = null;

	@GET
	@Path("checked/structure/{idStructure}/{offset}/{rownum}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Map> getStructurePropertyTypes(@PathParam("idStructure") Integer idStructure, @PathParam("offset") Integer offset,
			@PathParam("rownum") Integer rownum) {
		if(idStructure != null && idStructure > 0){
			return this.getStructurePropertyTypeService().findByIdStructure(idStructure, offset, rownum);	
		}else{
			return this.getStructurePropertyTypeService().findAll();
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Map insertStructurePropertyType(Map map) {
		Integer id_structure = null;
		Integer id_propertyType;
		Integer id = 0;

		id_structure = (Integer) map.get("idStructure");
		id_propertyType = (Integer) ((Map) map.get("propertyType")).get("id");

		this.getStructurePropertyTypeService().insert(id_structure, id_propertyType);
		// id =
		// this.getStructurePropertyTypeService().findIdByIdStructureAndIdPropertyType(id_structure,
		// id_propertyType);
		map.put("id", id);
		return map;
	}

	@DELETE
	@Path("{idStructure}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Integer deleteStructurePropertyType(@PathParam("idStructure") Integer id) {
		Integer count = 0;

		count = this.getStructurePropertyTypeService().delete(id);
		if (count == 0) {
			throw new NotFoundException(I18nUtils.getProperty("structurePropertyTypeDeleteErrorAction"));
		}
		return count;
	}

	public PropertyTypeService getPropertyTypeService() {
		return propertyTypeService;
	}

	public void setPropertyTypeService(PropertyTypeService propertyTypeService) {
		this.propertyTypeService = propertyTypeService;
	}

	public StructurePropertyTypeService getStructurePropertyTypeService() {
		return structurePropertyTypeService;
	}

	public void setStructurePropertyTypeService(StructurePropertyTypeService structurePropertyTypeService) {
		this.structurePropertyTypeService = structurePropertyTypeService;
	}
}
