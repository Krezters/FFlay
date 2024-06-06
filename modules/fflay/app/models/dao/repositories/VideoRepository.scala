package models.dao.repositories

import models.dao.schema.FFlaySchema
import models.dao.entities.Video
import org.squeryl.Table

trait VideoRepository {
  /**
   * @param video Video
   * @return Video
   */
  def insert(video: Video): Video

  /**
   * @param video Video
   * @return Unit
   */
  def upgrade(video: Video): Unit

  /**
   * @param id Int
   * @return Unit
   */
  def ready(id: Int): Unit

  /**
   * @param id Int
   * @return Option[Video]
   */
  def get(id: Int): Option[Video]

  /**
   * @param id Int
   * @return Option[Video]
   */
  def getByUserId(id: Int): List[Video]

  /**
   * @param count Int
   * @return List[Video]
   */
  def getAFew(count: Int): List[Video]

  /**
   * @param id Int
   * @return Unit
   */
  def delete(id: Int): Unit
}

class VideoRepositoryImpl extends VideoRepository {
  import org.squeryl.PrimitiveTypeMode._
  val videos: Table[Video] = FFlaySchema.videos

  /**
   * @param video Video
   * @return Video
   */
  override def insert(video: Video): Video = transaction(videos.insert(video))

  /**
   * @param video Video
   * @return Unit
   */
  override def upgrade(video: Video): Unit = videos.update(video)

  /**
   * @param id Int
   * @return Unit
   */
  def ready(id: Int): Unit = update(videos)(v => where(v.id === id) set(v.ready := true))

  /**
   * @param id Int
   * @return Option[Video]
   */
  override def get(id: Int): Option[Video] = transaction {
    from(videos)(v => where(v.id === id) select v).headOption
  }

  /**
   * @param id Int
   * @return List[Video]
   */
  override def getByUserId(id: Int): List[Video] = transaction {
    from(videos)(v => where(v.userId === id) select v).toList
  }

  /**
   * @param count Int
   * @return List[Video]
   */
  override def getAFew(count: Int): List[Video] = transaction {
    from(videos)(v => where(v.ready === true) select(v)).page(0, count).toList
  }

  /**
   * @param id Int
   * @return Unit
   */
  override def delete(id: Int): Unit = transaction(videos.deleteWhere(_.id === id))
}