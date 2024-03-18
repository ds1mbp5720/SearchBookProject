package com.example.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.datasource.BookDateSource
import com.example.data.mapper.toDomain
import com.example.data.paging.SearchPagingSource
import com.example.domain.model.BookDetailModel
import com.example.domain.model.BookModel
import com.example.domain.model.NewBookModel
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDateSource: BookDateSource
): BookRepository{
    override fun searchBook(query: String): Flow<PagingData<BookModel>> {
        return Pager(
            config = PagingConfig(pageSize = 10000), // 값 조정
            pagingSourceFactory = {
                SearchPagingSource(query = query, bookDataSource = bookDateSource) }
        ).flow
    }

    override fun getNewBookList(): Flow<NewBookModel> = flow {
        val response = bookDateSource.getNewBookList()
        emit(response.toDomain())
    }.retry {
        it is IllegalAccessException
    }.catch {e ->
        if(e is HttpException)
            throw e
    }

    override fun getDetailBook(isbn13: String): Flow<BookDetailModel> = flow {
        val response = bookDateSource.getDetailBook(
            isbn13 = isbn13
        )
        emit(response.toDomain())
    }.retry {
        it is IllegalAccessException
    }.catch {e ->
        if(e is HttpException)
            throw e
    }
}