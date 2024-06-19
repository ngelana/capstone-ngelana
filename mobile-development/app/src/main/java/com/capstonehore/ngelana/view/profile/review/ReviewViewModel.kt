package com.capstonehore.ngelana.view.profile.review

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.repository.GeneralRepository

class ReviewViewModel(private val repository: GeneralRepository): ViewModel() {

    fun getAllReviewByUserId() = repository.getAllReviewByUserId()

    fun createReview(reviewItem: ReviewItem) = repository.createReview(reviewItem)

}