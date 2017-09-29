#《NETTY官方文档》4.1的新特性及注意点
--
原文链接  译者：裘卡

此文档涵盖了netty4.0到4.1值得关注的变更点及新特性。

尽管我们尽力做到对4.0的向后兼容，4.1仍包含了一些无法完全向后兼容4.0的特性。请确保升级之后对应用进行重新编译。

在重新编译应用以后，你会看到一些deprecation的警告。请一定按照提示修改为相应的替代方案，以减少升级之后产生的问题。


##核心变更
__对Android的支持__

考虑到：

– 移动设备已经越来越强大了

– Ice Cream Sandwich版本已经修正了ADK中突出的NIO和SSLEngine的问题

– 用户希望可以在移动应用中复用codesc和handlers

因此我们决定对Android（4.0及以上）进行官方支持。

但是，我们现在仍然没有针对Android的自动化测试套件。如果发现Android相关的问题，烦请提交一个issue。也请考虑直接对项目进行贡献，使Android tests成为构建的一部分。

###ChannelHandlerContext.attr(..) == Channel.attr(..)

Channel 及 ChannelHandlerContext 都实现了 AttributeMap 接口，可以附加用户自定义属性。但由于 Channel 及 ChannelHandlerContext 的自定义属性存储是相对独立的，有时候就会对用户造成困扰。比如，当你通过Channel.attr(KEY_X).set(valueX)添加一个’KEY_X’属性时，你无法从ChannelHandlerContext.attr(KEY_X).get()获取到，反之亦然。这不仅是一种困扰，同时也是内存的浪费。

为了解决这个问题，我们决定每个 Channel 内部仅保留一个map。 AttributeMap 使用 AttributeKey 作为key。 AttributeKey 保证了各个key之间的唯一性，因此每个 Channel 就只会有一个属性map。当用户在他或她自己的 ChannelHandler 里面定义一个private static final的 AttributeKey 字段，就不会再有重复key的风险。

###Channel.hasAttr(…)

现在可以判断一个属性是否存在或者未生效了。

###buffer泄露跟踪将更加容易并且精准

原先，泄露的warning往往没什么用，也很难找到bufffer到底在哪儿泄露的。现在我们可以启用更高级的泄露报告机制了，代价则是增加一定的开销。

查看 Reference counted objects 了解更多。因为此特性十分重要，因此我们也将其移植到了4.0.14.Final版本中。

###PooledByteBufAllocator将成为默认allocator

尽管UnpooledByteBufAllocator有一些限制（limitation），4.x版本中，它仍然是默认的allocator。由于PooledByteBufAllocator已经广泛使用了（has been in the wild ）一段时间了，并且也有了更高级的buffer泄露跟踪机制，因此，是时候把其作为默认allocator了。

###全局唯一的channel ID

每个 Channel 都会有一个全局唯一的ID，生成规则如下：

MAC 地址(EUI-48 or EUI-64)，最好是全局唯一的
当前进程ID
System#currentTimeMillis()
System#nanoTime()
32-bit的随机integer
序列化递增的32-bit integer.
可以通过调用 Channel.id() 获取Channel的ID值。

###EmbeddedChannel易用性提升

现在EmbeddedChannel的readInbound()和readOutbound()会返回ad-hoc类型的参数，这样就不需要对返回值进行向下转型了。这样，你的单元测试代码会简洁很多。
<pre><code>
EmbeddedChannel ch = ...;

// 以前:
FullHttpRequest req = (FullHttpRequest) ch.readInbound();

// 此后:
FullHttpRequest req = ch.readInbound();
</pre></code>
###可使用Executor代替ThreadFactory


有些应用需要使用自己的 Executor 跑任务。而4.x版本则需要用户在创建event loop时指定ThreadFactory，以后就不需要了。

此项变更的更多说明，请参考 pull request #1762

###Class loader友好性

AttributeKey之类对运行于容器中的应用不友好的问题已解决。

###ByteBufAllocator.calculateNewCapacity()

ByteBuf容量扩增的计算逻辑已经从AbstractByteBuf中迁到了ByteBufAllocator中。因为ByteBufAllocator更能把握它所管理的buffers的容量计算。

###新的codecs及handlers
Binary memcache protocol codec
Compression codecs
BZip2
FastLZ
LZ4
LZF
DNS protocol codec
HAProxy protocol codec
MQTT protocol codec
SPDY/3.1 支持
STOMP codec
SOCKSx codec 支持版本 4, 4a, 及 5; 参见 socksx 包.
XmlFrameDecoder 支持XML文档流（streaming of XML documents）
JsonObjectDecoder 支持JSON对象流（streaming of JSON objects）
IP filtering handlers
##其他codec变更
###AsciiString

AscciiString 是只包含单字节字符的CharSequence实现类。对于处理US-ASCII及ISO-8859-1字符串会很有用。

比如，Netty中的HTTP codec及STOMP codec使用AsciiString来表示首部名（header names）。因为将其转换为ByteBuf时是没有额外开销的，因此，也能获得比String更佳的性能。

###TextHeaders（译者注：截至最新的netty-all 4.1.9.Final，并未发现此API，而5.0则含有此API）

TextHeaders 提供了HTTP首部风格（header-like）的通用字符串 mutimaps 数据结构。HttpHeaders也使用TextHeaders进行了重写。

MessageAggregator

MessageAggregator 能够像HttpObjectAggregator一样将批量的小消息聚合为大消息。HttpObjectAggregator同样也使用MessageAggregator进行了重写。

###HttpObjectAggregator可以更好的处理大尺寸（oversized）消息

4.0版本中，在客户端发送内容之前，是无法拒绝接收超大体积的HTTP消息的，即使在100-continue状态时也无法做到。

新版本增加了可覆盖的handleOversizedMessage方法，用户可以根据自己的需求进行覆写。默认情况下，会响应’413 Request Entity Too Large’并且关闭连接。

###ChunkedInput 及 ChunkedWriteHandler

ChunkedInput 新增了progress()及length()方法，分别代表流的传输进度及总长度。ChunkedWriteHandler 使用这些信息通知 ChannelProgressiveFutureListener

###SnappyFramedEncoder 及 SnappyFramedDecoder

这两个类重命名为 SnappyFrameEncoder 及 SnappyFrameEncoder。旧类已经被标记为已过时，并且实质上就是新类的子类。