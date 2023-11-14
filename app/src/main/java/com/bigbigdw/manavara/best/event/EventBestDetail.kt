package com.bigbigdw.manavara.best.event

import com.bigbigdw.manavara.best.models.ItemBestComment
import com.bigbigdw.manavara.best.models.ItemBestDetailInfo
import com.bigbigdw.manavara.best.models.ItemBestInfo
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

    class SetListComment(
        val listComment : ArrayList<ItemBestComment> = ArrayList()
    ) : EventBestDetail

    class SetListBestOther(
        val listBestOther : ArrayList<ItemBookInfo> = ArrayList()
    ) : EventBestDetail

    class SetListBestInfo(
        val listBestInfo : ArrayList<ItemBestInfo> = ArrayList()
    ) : EventBestDetail
}

data class StateBestDetail(
    val Loaded: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val itemBestDetailInfo : ItemBestDetailInfo = ItemBestDetailInfo(),
    val itemBestInfo : ItemBookInfo = ItemBookInfo(),
    val listComment : ArrayList<ItemBestComment> = ArrayList(),
    val listBestOther : ArrayList<ItemBookInfo> = ArrayList(),
    val listBestInfo : ArrayList<ItemBestInfo> = ArrayList(),
)