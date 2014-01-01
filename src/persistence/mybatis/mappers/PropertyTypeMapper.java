package persistence.mybatis.mappers;

import java.util.List;
import java.util.Map;

import model.PropertyType;

public interface PropertyTypeMapper {
	public Integer insertProperty(PropertyType property);

	public Integer updateProperty(PropertyType property);

	public Integer deleteProperty(Integer id);

	public List<PropertyType> findAll();

	public List<PropertyType> findPropertysByIdStructure(Integer id_structure);

	public List<Integer> findPropertyIdsByIdStructure(Integer id_structure);

	public Integer findIdStructureByIdProperty(Integer idProperty);

	public List<PropertyType> findPropertysByIdStructureAndOffsetAndRownum(Map map);

	public List<PropertyType> search(Map map);

	public PropertyType findPropertyById(Integer id);

	public PropertyType findPropertyByIdStructureAndName(Map map);
}
