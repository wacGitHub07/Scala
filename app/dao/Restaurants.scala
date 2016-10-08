package dao

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play
import slick.lifted.TableQuery
import model.RestaurantTableDef
import model.CompleteRestaurant
import scala.concurrent.Future
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.impl.Future
import scala.concurrent.impl.Future

//El Object convierte la clase en Singleton
object Restaurants {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val restaurants = TableQuery[RestaurantTableDef]
  def list:Future[Seq[CompleteRestaurant]]={
    dbConfig.db.run(restaurants.result)
  }
  
  def getById(id:Long):Future[Option[CompleteRestaurant]]={
    dbConfig.db.run(restaurants.filter(_.id === id).result.headOption)
  }
  
  def save(restaurant:CompleteRestaurant):Future[String]={
    dbConfig.db.run(restaurants+=restaurant).map(res => "Restaurant saved").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }
  
  def update(restaurant:CompleteRestaurant):Future[Int]={
    dbConfig.db.run(restaurants.filter(_.id===restaurant.id).update(restaurant))
  }
  
  def delete(restaurant:Long):Future[Int]={
    dbConfig.db.run(restaurants.filter(_.id===restaurant).delete)
  }
}