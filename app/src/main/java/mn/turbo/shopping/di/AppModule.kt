package mn.turbo.shopping.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mn.turbo.shopping.common.Constants
import mn.turbo.shopping.data.local.ShoppingDao
import mn.turbo.shopping.data.local.ShoppingItemDatabase
import mn.turbo.shopping.data.remote.PixabayAPI
import mn.turbo.shopping.repository.DefaultShoppingRepository
import mn.turbo.shopping.repository.ShoppingRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ): ShoppingItemDatabase =
        Room.databaseBuilder(context, ShoppingItemDatabase::class.java, Constants.DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideShoppingRepository(
        dao: ShoppingDao,
        api: PixabayAPI
    ): ShoppingRepository {
        return DefaultShoppingRepository(dao, api)
    }
}