/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:JvmMultifileClass
@file:JvmName("MessageUtils")

package net.mamoe.mirai.message.data

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.utils.MiraiInternalAPI
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 * 群内的引用回复.
 * 总是使用 [quote] 来构造实例.
 */
class QuoteReply @MiraiInternalAPI constructor(val source: MessageSource) : Message {
    companion object Key : Message.Key<QuoteReply>

    override fun toString(): String = ""
}

/**
 * 引用这条消息.
 * 返回 `[QuoteReply] + [At] + [PlainText]`(必要的结构)
 */
@UseExperimental(MiraiInternalAPI::class)
fun MessageChain.quote(sender: QQ): MessageChain {
    this.firstOrNull<MessageSource>()?.let {
        return if (it.groupId == 0L) {
            QuoteReply(it).toChain() // required
        } else {
            check(sender is Member) { "sender must be Member to quote a GroupMessage" }
            QuoteReply(it) + sender.at() + " " // required
        }
    }
    error("cannot find MessageSource")
}

/**
 * 引用这条消息.
 * 返回 `[QuoteReply] + [At] + [PlainText]`(必要的结构)
 */
@UseExperimental(MiraiInternalAPI::class)
fun MessageSource.quote(sender: QQ): MessageChain {
    return if (this.groupId == 0L) {
        QuoteReply(this) + " " // required
    } else {
        check(sender is Member) { "sender must be Member to quote a GroupMessage" }
        QuoteReply(this) + sender.at() + " " // required
    }
}