package com.capstonehore.ngelana.view.profile.review

import androidx.lifecycle.ViewModel
import com.capstonehore.ngelana.data.remote.response.ReviewItem
import com.capstonehore.ngelana.data.repository.Repository
import com.capstonehore.ngelana.data.repository.ReviewRepository

class ReviewViewModel(
    private val reviewRepository: ReviewRepository
//    private val repository: Repository
): ViewModel() {

    fun getAllReviewByUserId() = reviewRepository.getAllReviewByUserId()

    fun createReview(reviewItem: ReviewItem) = reviewRepository.createReview(reviewItem)

}