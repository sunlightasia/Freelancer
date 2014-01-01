package service;

import java.util.List;

import model.PropertyType;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PropertyTypeService {
	public Integer insertProperty(PropertyType property);

	public Integer updateProperty(PropertyType property);

	public Integer deleteProperty(Integer id);

	public List<PropertyType> findAll();

	public List<PropertyType> findPropertysByIdStructure(Integer id_structure);

	public List<Integer> findPropertyIdsByIdStructure(Integer id_structure);

	public List<PropertyType> findPropertysByIdStructure(Integer id_structure, Integer offset, Integer rownum);

	public PropertyType findPropertyById(Integer id);

	public PropertyType findPropertyByIdStructureAndName(Integer id_structure, String name);

	public Integer findIdStructureByIdProperty(Integer idProperty);
}
