package com.example.foodapp.di

import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.CategoryRepository
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.PreviewRepository
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.PromotionRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.repository.AuthRepositoryImpl
import com.example.foodapp.domain.repository.CartRepositoryImpl
import com.example.foodapp.domain.repository.CategoryRepositoryImpl
import com.example.foodapp.domain.repository.ChatRepositoryImpl
import com.example.foodapp.domain.repository.FavoriteRepositoryImpl
import com.example.foodapp.domain.repository.FoodRepositoryImpl
import com.example.foodapp.domain.repository.NotificationRepositoryImpl
import com.example.foodapp.domain.repository.OrderRepositoryImpl
import com.example.foodapp.domain.repository.PreviewRepositoryImpl
import com.example.foodapp.domain.repository.ProfileRepositoryImpl
import com.example.foodapp.domain.repository.PromotionRepositoryImpl
import com.example.foodapp.domain.repository.RestaurantRepositoryImpl
import com.example.foodapp.domain.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        impl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindFoodRepository(
        impl: FoodRepositoryImpl
    ): FoodRepository


    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        impl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(
        impl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindPromotionRepository(
        impl: PromotionRepositoryImpl
    ): PromotionRepository


    @Binds
    @Singleton
    abstract fun bindPreviewRepository(
        impl: PreviewRepositoryImpl
    ): PreviewRepository
}
