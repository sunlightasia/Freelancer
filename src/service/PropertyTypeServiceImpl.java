package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.PropertyType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import persistence.mybatis.mappers.PropertyTypeMapper;

@Service
public class PropertyTypeServiceImpl implements PropertyTypeService {
	@Autowired
	private PropertyTypeMapper propertyTypeMapper = null;
	@Autowired
	private FacilityService facilityService = null;
	@Autowired
	private StructureService structureService = null;
	@Autowired
	private ImageService imageService = null;

	@Override
	public List<PropertyType> findAll() {
		return this.getPropertyTypeMapper().findAll();
	}

	@Override
	public List<PropertyType> findPropertysByIdStructure(Integer id_structure) {
		List<PropertyType> ret = null;

		ret = this.getPropertyTypeMapper().findPropertysByIdStructure(id_structure);
		return ret;
	}

	@Override
	public List<Integer> findPropertyIdsByIdStructure(Integer id_structure) {
		return this.getPropertyTypeMapper().findPropertyIdsByIdStructure(id_structure);
	}

	@Override
	public List<PropertyType> findPropertysByIdStructure(Integer id_structure, Integer offset, Integer rownum) {
		Map map = null;

		map = new HashMap();
		map.put("id_structure", id_structure);
		map.put("offset", offset);
		map.put("rownum", rownum);
		return this.getPropertyTypeMapper().findPropertysByIdStructureAndOffsetAndRownum(map);
	}

	public PropertyTypeMapper getPropertyTypeMapper() {
		return propertyTypeMapper;
	}

	public void setPropertyTypeMapper(PropertyTypeMapper propertyTypeMapper) {
		this.propertyTypeMapper = propertyTypeMapper;
	}

	@Override
	public PropertyType findPropertyById(Integer id) {
		PropertyType property = null;
		// List<Image> images = null;

		property = this.getPropertyTypeMapper().findPropertyById(id);
		// images = this.getImageService().findCheckedByIdProperty(id);
		// property.setImages(images);
		return property;

	}

	@Override
	public PropertyType findPropertyByIdStructureAndName(Integer id_structure, String name) {
		Map map = null;

		map = new HashMap();
		map.put("id_structure", id_structure);
		map.put("name", name);
		return this.getPropertyTypeMapper().findPropertyByIdStructureAndName(map);
	}

	@Override
	public Integer findIdStructureByIdProperty(Integer idProperty) {
		return this.getPropertyTypeMapper().findIdStructureByIdProperty(idProperty);
	}

	@Override
	public Integer insertProperty(PropertyType property) {
		Integer ret = 0;

		ret = this.getPropertyTypeMapper().insertProperty(property);
		return ret;
	}

	@Override
	public Integer updateProperty(PropertyType property) {
		Integer ret = 0;

		ret = this.getPropertyTypeMapper().updateProperty(property);
		return ret;
	}

	@Override
	public Integer deleteProperty(Integer id) {
		return this.getPropertyTypeMapper().deleteProperty(id);
	}

	public FacilityService getFacilityService() {
		return facilityService;
	}

	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}

	public StructureService getStructureService() {
		return structureService;
	}

	public void setStructureService(StructureService structureService) {
		this.structureService = structureService;
	}

	public ImageService getImageService() {
		return imageService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

}
