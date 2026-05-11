package com.example.foodapp.presentation.extentions

import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Restaurant

class RestaurantExtentions {
    //format 4.5 -> "4.5"

    fun Restaurant.getFormattedRating(): String {
        return String.format("%.1f", getAverageRating())
    }

    fun Restaurant.getRattingText(): String {
        return if (totalReview > 0) {
            "⭐ ${getFormattedRating()} ($totalReview)"
        } else {
            "chưa có đánh giá"
        }
    }

    fun Restaurant.getFormattedDeliveryFee(): String {
        return if (deliveryFee > 0) {
            "${deliveryFee.toVND()}d"
        } else {
            "Miễn phí"
        }
    }

    fun Restaurant.getFormattedMinimum(): String {
        return "Đơn tối thiểu: ${minOrderAmount.toVND()}đ"
    }

    fun Restaurant.getTimeText(): String {
        return "$estimatedDeliveryTime phut"
    }

    fun Restaurant.getOperatingHouse(): String {
        return "$openingHours - $closingHours"
    }

    fun Restaurant.getOpenStatusText(): String {
        return if (isOpen) {
            "Đang mở cửa"
        } else {
            "Đang đóng cửa"
        }
    }

    ////----------------
    fun Restaurant.getFormattedDistance(
        userLat: Double,
        userLng: Double
    ): String {
        val distance = calculateDistance(userLat, userLng  )
        return when {
            distance == 0.0 -> "Vị trí hiện tại"
            distance < 1 -> "${(distance * 1000).toInt()}m"
            else -> String.format("%1.fkm", distance)
        }
    }fun Restaurant.getInfoText(userLat: Double = 0.0, userLng: Double = 0.0): String {
        val parts = mutableListOf<String>()

        if (totalReview > 0) {
            parts.add("⭐ ${getFormattedRating()} ($totalReview)")
        }

        parts.add(getTimeText())

        if (userLat != 0.0 && userLng != 0.0) {
            val distanceText = getFormattedDistance(userLat, userLng)
            if (distanceText.isNotEmpty()) {
                parts.add(distanceText)
            }
        }

        return parts.joinToString(" • ")
    }

    /**
     * Get delivery info
     * VD: "Miễn phí • 30 phút"
     */
    fun Restaurant.getDeliveryInfo(): String {
        return "${getFormattedDeliveryFee()} • ${getTimeText()}"
    }

    /**
     * Get badge text (nếu có)
     */
    fun Restaurant.getBadgeText(): String? {
        return when {
            isHighlyRated() -> "Đánh giá cao"
            isPopular() -> "Phổ biến"
            isDeliveryFree() -> "Miễn phí ship"
            else -> null
        }
    }

// ============================================
// HELPER
// ============================================

    private fun Int.toVND(): String {
        return "%,d".format(this).replace(",", ".")
    }

}