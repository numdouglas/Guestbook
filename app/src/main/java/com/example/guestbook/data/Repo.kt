package com.example.guestbook.data

import android.content.Context
import android.net.Uri
import androidx.room.*

@Entity
data class Guest(
    val name:String,
    val address:String,
    @PrimaryKey
    val phone:Int,
    val email:String,
    val comment:String,
    val picture: String
)

@Dao
interface GuestDao{
    @Query("Select * from Guest")
    fun getAll():List<Guest>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(guest:Guest):Long

    @Query("delete from Guest")
    fun deleteAll()

    @Delete
    fun delete(guest:Guest)

    @Query("delete from Guest where phone = :phoneNo ")
    fun deleteByPhoneNumber(phoneNo:Int):Int

}

@Database(entities = [Guest::class],version = 1)
abstract class GuestRepo:RoomDatabase(){
    abstract fun guestDao():GuestDao

    companion object{
        @Volatile
        private var INSTANCE:GuestRepo?=null


        fun getDatabase(context: Context):GuestRepo{
            val tempInstance= INSTANCE

            tempInstance.let {tempInstance}

            synchronized(this){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    GuestRepo::class.java,
                    "guest_database"
                ).build()
            INSTANCE=instance
            return instance
            }
        }
    }
}