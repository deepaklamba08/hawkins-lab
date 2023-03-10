package org.stranger.common.util;

/**
 * Stranger constants
 */
public class StrangerConstants {
    public static final String APP_EXE_RUNNING_MESSAGE = "application is running";
    public static final String APP_EXE_SUCCESS_MESSAGE = "application succeeded";
    public static final String APP_EXE_DS_LOAD_FAILED_MESSAGE = "data source load failed.";
    public static final String APP_EXE_SESSION_CREATION_FAILED_MESSAGE = "session creation failed";
    public static final String APP_EXE_TR_EXE_FAILED_MESSAGE = "transformation execution failed.";
    public static final String APP_EXE_SINK_EXE_FAILED_MESSAGE = "sink execution failed.";
    public static final String SPARK_CONFIG_FIELD = "spark_config";

    public static final String EXECUTION_ENGINE_SPARK = "spark";

    public static final String REPO_TYPE_JSON = "json";
    public static final String APP_PARAM_ID = "appId";
    public static final String APP_PARAM_SUBMITTER = "submitter";

    /**
     * config constants
     */
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String CREATE_DATE_FIELD = "createDate";
    public static final String UPDATE_DATE_FIELD = "updateDate";
    public static final String CREATED_BY_FIELD = "createdBy";
    public static final String UPDATED_BY_FIELD = "updatedBy";
    public static final String IS_ACTIVE_FIELD = "isActive";
    public static final String TYPE_FIELD = "type";
    public static final String CONFIGURATION_FIELD = "configuration";
    public static final String SOURCES_FIELD = "sources";
    public static final String INDEX_FIELD = "index";
    public static final String FILE_FORMAT_FIELD = "fileFormat";
    public static final String MODE_FIELD = "mode";
    public static final String LOCATION_FIELD = "location";
    public static final String VIEW_FIELD = "view";
    public static final String IS_PERSIST_FIELD = "isPersist";
    public static final String PERSIST_MODE_FIELD = "persistMode";
    public static final String REPARTITION_FIELD = "repartition";
    public static final String RESTRUCTURE_ON_FIELD = "restructureOn";
    public static final String NUM_PARTITIONS_FIELD = "numPartitions";
    public static final String COLUMNS_FIELD = "columns";

    /**
     * storage level
     */
    public static final String PERSIST_MODE_DISK_ONLY = "disk_only";
    public static final String PERSIST_MODE_DISK_ONLY_2 = "disk_only_2";
    public static final String PERSIST_MODE_MEMORY_ONLY = "memory_only";
    public static final String PERSIST_MODE_MEMORY_ONLY_2 = "memory_only_2";
    public static final String PERSIST_MODE_MEMORY_ONLY_SER = "memory_only_ser";
    public static final String PERSIST_MODE_MEMORY_ONLY_SER_2 = "memory_only_ser_2";
    public static final String PERSIST_MODE_MEMORY_AND_DISK = "memory_and_disk";
    public static final String PERSIST_MODE_MEMORY_AND_DISK_2 = "memory_and_disk_2";
    public static final String PERSIST_MODE_MEMORY_AND_DISK_SER = "memory_and_disk_ser";
    public static final String PERSIST_MODE_MEMORY_AND_DISK_SER_2 = "memory_and_disk_ser_2";
    public static final String PERSIST_MODE_OFF_HEAP = "off_heap";

    public static final String VIEW_TYPE_GLOBAL_TEMP = "global_temp";
    public static final String TRANSFORMATIONS_FIELD = "transformations";
    public static final String QUERY_TYPE_FIELD = "queryType";
    public static final String VALUE_FIELD = "value";
    public static final String SINKS_FIELD = "sinks";
    public static final String IMPLEMENTATION_FIELD = "implementation";

    public static final String REPARTITION_ON_COLUMN = "column";
    public static final String REPARTITION_ON_SIZE = "size";
    public static final String REPARTITION_ON_BOTH = "both";
    public static final String QUERY_SOURCE_SQL = "sql";
    public static final String QUERY_SOURCE_FILE = "file";

    /**
     * source types
     */
    public static final String SOURCE_TYPE_FILE = "file";
    /**
     * dink types
     */
    public static final String SINK_TYPE_FILE = "file";

    /**
     * transformation types
     */
    public static final String TRANSFORMATION_TYPE_SQL = "sql";
    public static final String TRANSFORMATION_TYPE_CUSTOM = "custom";

}
