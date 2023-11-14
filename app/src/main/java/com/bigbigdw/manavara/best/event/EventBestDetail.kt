package com.bigbigdw.manavara.best.event

import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBookInfo
import com.bigbigdw.manavara.main.models.UserInfo

sealed interface EventBestDetail{
    object Loaded: EventBestDetail

    class SetUserInfo(
        val userInfo: UserInfo = UserInfo(),
    ) : EventBestDetail

    class SetItemBestDetailInfo(
        val itemBestDetailInfo : ItemBestDetailInfo = ItemBestDetailInfo()
    ) : EventBestDetail

    class SetItemBookInfo(
        val itemBestInfo : ItemBookInfo = ItemBookInfo()
    ) : EventBestDetail
}

data class StateBestDetail(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val itemBestDetailInfo : ItemBestDetailInfo = ItemBestDetailInfo(),
    val itemBestInfo : ItemBookInfo = ItemBookInfo()
)