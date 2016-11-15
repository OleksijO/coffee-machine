package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.GenericDao;

abstract class AbstractDao<T> implements GenericDao<T> {
	static final String ERROR_PARSE_RESULT_SET = "Error while parsing result set for ";
	static final String ERROR_PREPARING_INSERT = "Error while preparing INSERT statement for ";
	static final String ERROR_PREPARING_UPDATE = "Error while preparing UPDATE statement for ";
	static final String ERROR_ID_MUST_BE_FROM_DBMS = "Id must be obtained from DB, cannot create record in ";
	static final String ERROR_SELECT_ALL = "Error while reading all records";
	static final String ERROR_SELECT_1 = "Error while reading record by key";

	static final String FIELD_ID = "id";



}
