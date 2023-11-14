package com.bigbigdw.moavara.Retrofit

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//검색결과
class SearchResultKakao {

    @SerializedName("results")
    @Expose
    val results: List<KakaoBookResult>? = null

}

class KakaoBookResult {
    @SerializedName("items")
    @Expose
    var items: List<KakaoBookItem>? = null
}

class KakaoBookItem {
    @SerializedName("author")
    @Expose
    var author: String = ""

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("image_url")
    @Expose
    var image_url: String = ""

    @SerializedName("publisher_name")
    @Expose
    var publisher_name: String = ""

    @SerializedName("sub_category")
    @Expose
    var sub_category: String = ""

    @SerializedName("read_count")
    @Expose
    var read_count: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("page")
    @Expose
    var page: String = ""

}

class BestResultKakaoStageNovel {
    @SerializedName("novel")
    @Expose
    val novel: KakaoBestStageResult? = null
}

class KakaoBestStageResult {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("subGenre")
    @Expose
    lateinit var subGenre: KakaoBestStageSubGenre

    @SerializedName("stageSeriesNumber")
    @Expose
    var stageSeriesNumber: String = ""

    @SerializedName("nickname")
    @Expose
    var nickname: KakaoBestStageNickNameResult? = null

    @SerializedName("synopsis")
    @Expose
    var synopsis: String = ""

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: KakaoBestStageThumbnailResult? = null

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String = ""

    @SerializedName("viewCount")
    @Expose
    var viewCount: String = ""

    @SerializedName("visitorCount")
    @Expose
    var visitorCount: String = ""

    @SerializedName("episodeLikeCount")
    @Expose
    var episodeLikeCount: String = ""

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""
}

class KakaoBestStageThumbnailResult {
    @SerializedName("url")
    @Expose
    var url: String = ""
}

class KakaoBestStageNickNameResult {
    @SerializedName("name")
    @Expose
    var name: String = ""
}

//베스트 결과
class BestResultKakao {
    @SerializedName("list")
    @Expose
    val list: List<KakaoBestBookResult>? = null
}

class KakaoBestBookResult {
    @SerializedName("author")
    @Expose
    var author: String = ""

    @SerializedName("sub_category_title")
    @Expose
    var sub_category_title: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("like_count")
    @Expose
    var like_count: String = ""

    @SerializedName("rating")
    @Expose
    var rating: String = ""

    @SerializedName("series_id")
    @Expose
    var series_id: String = ""

    @SerializedName("read_count")
    @Expose
    var read_count: String = ""

    @SerializedName("caption")
    @Expose
    var caption: String = ""

    @SerializedName("image")
    @Expose
    var image: String = ""

    @SerializedName("comment_count")
    @Expose
    var comment_count: String = ""
}

class BestKakaoBookDetail {
    @SerializedName("home")
    @Expose
    val home: BestKakaoBookDetailHome? = null

    @SerializedName("notice")
    @Expose
    val notice: BestKakaoBookDetailNotice? = null

    @SerializedName("related_keytalk_list")
    @Expose
    var related_keytalk_list = ArrayList<BestKakaoBookDetailKeyword>()
}

class BestKakaoBookDetailKeyword{
    @SerializedName("item_name")
    @Expose
    var item_name: String = ""
}

class BestKakaoBookDetailNotice {
    @SerializedName("create_dt")
    @Expose
    var create_dt: String = ""

    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""
}

class BestKakaoBookDetailHome {
    @SerializedName("description")
    @Expose
    var description: String = ""

    @SerializedName("author_name")
    @Expose
    var author_name: String = ""

    @SerializedName("page_comment_count")
    @Expose
    var page_comment_count: String = ""

    @SerializedName("page_rating_count")
    @Expose
    var page_rating_count: String = ""

    @SerializedName("page_rating_summary")
    @Expose
    var page_rating_summary: String = ""

    @SerializedName("read_count")
    @Expose
    var read_count: String = ""

    @SerializedName("sub_category")
    @Expose
    var sub_category: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("land_thumbnail_url")
    @Expose
    var land_thumbnail_url: String = ""

    @SerializedName("open_counts")
    @Expose
    var open_counts: String = ""
}

class BestKakaoBookDetailComment {
    @SerializedName("comment_list")
    @Expose
    var comment_list = ArrayList<BestKakaoBookDetailCommentList>()
}

class BestKakaoBookDetailCommentList {
    @SerializedName("comment")
    @Expose
    var comment: String = ""

    @SerializedName("create_dt")
    @Expose
    var create_dt: String = ""
}

class KakaoStageBestBookResult {
    @SerializedName("synopsis")
    @Expose
    var synopsis: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String = ""

    @SerializedName("viewCount")
    @Expose
    var viewCount: String = ""

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""

    @SerializedName("visitorCount")
    @Expose
    var visitorCount: String = ""

    @SerializedName("nickname")
    @Expose
    lateinit var nickname: KakaoStageBestBookNickname

    @SerializedName("subGenre")
    @Expose
    lateinit var subGenre: KakaoBestStageSubGenre

    @SerializedName("thumbnail")
    @Expose
    lateinit var thumbnail: KakaoStageBestBookThumbnail

    @SerializedName("episodeLikeCount")
    @Expose
    var episodeLikeCount: String = ""

}

class KakaoStageBestBookNickname {
    @SerializedName("name")
    @Expose
    var name: String = ""
}

class KakaoStageBestBookThumbnail {
    @SerializedName("url")
    @Expose
    var url: String = ""
}

class KakaoStageBestBookCommentResult {
    @SerializedName("content")
    @Expose
    var content = ArrayList<KakaoStageBestBookCommentContents>()
}

class KakaoStageBestBookCommentContents {
    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("createdAt")
    @Expose
    var createdAt: String = ""
}

class KakaoStageSearchResult {
    @SerializedName("content")
    @Expose
    var content = ArrayList<KakaoStageSearchContents>()
}

class KakaoStageSearchContents {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("nickname")
    @Expose
    var nickname = KakaoBestStageNickNameResult()

    @SerializedName("thumbnail")
    @Expose
    var thumbnail = KakaoBestStageThumbnailResult()

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""

    @SerializedName("viewCount")
    @Expose
    var viewCount: String = ""

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String = ""

    @SerializedName("stageSeriesNumber")
    @Expose
    var stageSeriesNumber: String = ""

    @SerializedName("synopsis")
    @Expose
    var synopsis: String = ""

    @SerializedName("subGenre")
    @Expose
    var subGenre = KakaoBestStageSubGenre()
}

class KakaoBestStageSubGenre {
    @SerializedName("name")
    @Expose
    var name: String = ""
}

class KakaoStageEventList {
    @SerializedName("content")
    @Expose
    var content = ArrayList<KakaoStageEventListContents>()

    @SerializedName("totalElements")
    @Expose
    var totalElements: String = ""
}

class KakaoStageEventListContents {
    @SerializedName("active")
    @Expose
    var active: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("startedAt")
    @Expose
    var startedAt: String = ""

    @SerializedName("endedAt")
    @Expose
    var endedAt: String = ""

    @SerializedName("desktopListImage")
    @Expose
    var desktopListImage: KakaoStageEventListDesktopListImage? = null

    @SerializedName("desktopDetailImage")
    @Expose
    var desktopDetailImage: KakaoStageEventListDesktopDetailImage? = null
}

class KakaoStageEventListDesktopListImage {

    @SerializedName("image")
    @Expose
    var image: KakaoStageEventListDesktopListImageImage? = null
}

class KakaoStageEventListDesktopListImageImage{
    @SerializedName("url")
    @Expose
    var url: String = ""
}

class KakaoStageEventListDesktopDetailImage{
    @SerializedName("image")
    @Expose
    var image: KakaoStageEventListDesktopListImageImage? = null
}

class BestKakao2Result {
    @SerializedName("pageProps")
    @Expose
    val pageProps: BestKakaoPageProps? = null
}

class BestKakaoPageProps {
    @SerializedName("initialState")
    @Expose
    val initialState: BestKakaoInitialState? = null
}

class BestKakaoInitialState {
    @SerializedName("json")
    @Expose
    val json: BestKakaoJson? = null
}

class BestKakaoJson {
    @SerializedName("pagewebLayout")
    @Expose
    val pagewebLayout: BestKakaoPagewebLayout? = null
}

class BestKakaoPagewebLayout {
    @SerializedName("entities")
    @Expose
    val entities: BestKakaoEntities? = null
}

class BestKakaoEntities {
    @SerializedName("items")
    @Expose
    val items = JsonObject()
}

class BestKakaoItemsProps {
    @SerializedName("NormalList-RankingItemSeriesDto-47050646-8")
    @Expose
    val items8: BestKakaoItems? = null
}


class BestKakaoItems {
    @SerializedName("thumbnail")
    @Expose
    val thumbnail = ""

    @SerializedName("row1")
    @Expose
    val row1 = ""

    @SerializedName("row2")
    @Expose
    val row2 : ArrayList<String>? = null

    @SerializedName("scheme")
    @Expose
    val scheme = ""
}
