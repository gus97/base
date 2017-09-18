A First Level Header
====================
A Second Level Header
---------------------

Now is the time for all good men to come to
the aid of their country. This is just a
regular paragraph.

The quick brown fox jumped over the lazy
dog's back.
### Header 3

> This is a blockquote.
> 
> This is the second paragraph in the blockquote.
>
> ## This is an H2 in a blockquote

Some of these words *are emphasized*.

Some of these words _are emphasized also_.

Use two asterisks for **strong emphasis**.

Or, if you prefer, __use two underscores instead__.

* Candy.
* Gum.
* Booze.

This is an [example link](http://example.com/).


<p>这是一个普通段落：</p>

<pre><code>
package kafka.admin

/**
  * Broker metadata used by admin tools.
  *
  * @param id an integer that uniquely identifies this broker
  * @param rack the rack of the broker, which is used to in rack aware partition assignment for fault tolerance.
  *             Examples: "RACK1", "us-east-1d"
  */
case class BrokerMetadata(id: Int, rack: Option[String])
</code></pre>