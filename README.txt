zookeeper Client常用命令:
1.ls path：列出path下的文件 获取根节点     ls /
2.stat path：查看节点状态
3.get path：获取指定节点的内容
4.ls2 path：列出path节点的子节点及状态信息
5.delete path [version] 删除节点


1.ZooKeeper对象的create方法用于创建znode.
	String create(String path, byte[] data, List acl, CreateMode createMode);  
 path. znode的路径.
 data. 与znode关联的数据.
 acl. 指定权限信息, 如果不想指定权限, 可以传入Ids.OPEN_ACL_UNSAFE.
 指定znode节点类型. CreateMode是一个枚举类, 从中选择一个成员传入即可.
 	持久节点（PERSISTENT） 所谓持久节点，是指在节点创建后，就一直存在，直到有删除操作来主动清除这个节点——不会因为创建该节点的客户端会话失效而消失。
 	持久顺序节点（PERSISTENT_SEQUENTIAL） 这类节点的基本特性和上面的节点类型是一致的。额外的特性是，在ZK中，每个父节点会为他的第一级子节点维护一份时序，会记录每个子节点创建的先后顺序。基于这个特性，在创建子节点的时候，可以设置这个属性，那么在创建节点过程中，ZK会自动为给定节点名加上一个数字后缀，作为新的节点名。这个数字后缀的范围是整型的最大值。
	临时节点（EPHEMERAL） 和持久节点不同的是，临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉。注意，这里提到的是会话失效，而非连接断开。另外，在临时节点下面不能创建子节点。
	临时顺序节点（EPHEMERAL_SEQUENTIAL） 临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉。注意创建的节点会自动加上编号

2.ZooKeeper对象的getData方法用于获取node关联的数据 参数的含义 返回值类型是字节流
	byte[] getData(String path, boolean watch, Stat stat); 
 watch参数用于指定是否监听path node的删除事件, 以及数据更新事件, 注意, 不监听path node的创建事件, 因为如果path node不存在, 该方法将抛出KeeperException.NoNodeException异常.
 stat参数是个传出参数, getData方法会将path node的状态信息设置到该参数中.

3.ZooKeeper对象的setData方法用于更新node关联的数据.
	Stat setData(final String path, byte data[], int version);  
 data为待更新的数据.
 version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查.
 返回path node的状态信息.
 
4.获取子node列表 ZooKeeper对象的getChildren方法用于获取子node列表.
	List getChildren(String path, boolean watch);  
 watch参数用于指定是否监听path node的子node的增加和删除事件, 以及path node本身的删除事件.
 
5.判断znode是否存在 ZooKeeper对象的exists方法用于判断指定znode是否存在.
	Stat exists(String path, boolean watch);  
 watch参数用于指定是否监听path node的创建, 删除事件, 以及数据更新事件. 如果该node存在, 则返回该node的状态信息, 否则返回null.


