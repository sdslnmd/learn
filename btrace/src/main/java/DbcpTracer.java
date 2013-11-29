@BTrace  
public class DbcpTracer {  
  
    @OnMethod(clazz = "org.apache.commons.pool.impl.GenericObjectPool", method = "/.*Object/", location = @Location(value = Kind.ENTRY))  
    public static void poolMonitor(@Self Object self) {  
        Field maxActiveField = field("org.apache.commons.pool.impl.GenericObjectPool", "_maxActive");  
        Field numActiveField = field("org.apache.commons.pool.impl.GenericObjectPool", "_numActive");  
        Field poolField = field("org.apache.commons.pool.impl.GenericObjectPool", "_pool");  
        Field sizeField = field("org.apache.commons.pool.impl.CursorableLinkedList", "_size");  
        int maxActive = (Integer) get(maxActiveField, self);  
        int numActive = (Integer) get(numActiveField, self);  
        int numIdle = (Integer) get(sizeField, get(poolField, self));  
  
        println(strcat(strcat(strcat(strcat(strcat("maxActive : ", str(maxActive)), " numActive : "), str(numActive)),  
                              " numIdle : "), str(numIdle)));  
    }  
  
    @OnMethod(clazz = "org.apache.commons.pool.impl.GenericKeyedObjectPool", method = "/.*Object/", location = @Location(value = Kind.ENTRY))  
    public static void psMonitor(@Self Object self, Object key) {  
        Field maxTotalField = field("org.apache.commons.pool.impl.GenericKeyedObjectPool", "_maxTotal"); // connectio的maxActive  
        Field totalActiveField = field("org.apache.commons.pool.impl.GenericKeyedObjectPool", "_totalActive"); // connectio的active  
        Field poolMapField = field("org.apache.commons.pool.impl.GenericKeyedObjectPool", "_poolMap"); // connectio的active  
  
        Field keyActiveField = field("org.apache.commons.pool.impl.GenericKeyedObjectPool$ObjectQueue", "activeCount"); // key的active  
        Field keyIdleField = field("org.apache.commons.pool.impl.GenericKeyedObjectPool$ObjectQueue", "queue"); // key的idle  
        Field keyIdleSizeField = field("org.apache.commons.pool.impl.CursorableLinkedList", "_size");  
  
        Field sqlField = field("org.apache.commons.dbcp.PoolingConnection$PStmtKey", "_sql");  
  
        int maxTotal = (Integer) get(maxTotalField, self);  
        int totalActive = (Integer) get(totalActiveField, self);  
        Map<Object, Object> poolMap = (Map<Object, Object>) get(poolMapField, self);  
        int keyActive = 0, keyIdle = 0;  
        if (poolMap != null) {  
            Object queue = get(poolMap, key);  
            if (queue != null) { // ObjectQueue  
                keyActive = (Integer) get(keyActiveField, queue);  
                keyIdle = (Integer) get(keyIdleSizeField, get(keyIdleField, queue));  
            }  
        }  
        println(strcat(strcat(strcat(strcat(strcat(strcat(strcat("maxTotal : ", str(maxTotal)), " totalActive : "),  
                                                   str(totalActive)), " keyActive : "), str(keyActive)), " keyIdle "),  
                       str(keyIdle)));  
  
        println(strcat("Ps Key: ", str(get(sqlField, key))));  
    }  
  
}  