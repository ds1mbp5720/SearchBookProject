package com.example.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.datasource.BookDateSource
import com.example.data.mapper.toDomain
import com.example.domain.model.BookModel
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource (
    private val query: String = "",
    private val bookDataSource: BookDateSource,
) : PagingSource<Int,BookModel>(){
    override fun getRefreshKey(state: PagingState<Int, BookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookModel> {
        return try{
            val currentPage = params.key ?: 1
            val bookList = bookDataSource.searchBook(
                query = query,
                page = currentPage.toString()
            ).toDomain()
            if(bookList.books?.isNotEmpty() == true){
                LoadResult.Page(
                    data = bookList.books!!,
                    prevKey = if(currentPage == 1) null else currentPage - 1,
                    nextKey = if(bookList.books!!.isEmpty()) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}