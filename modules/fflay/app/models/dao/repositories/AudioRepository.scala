package models.dao.repositories

import models.dao.schema.FFlaySchema
import models.dao.entities.Audio
import org.squeryl.Table

trait AudioRepository {
  /**
   * @param audio Audio
   * @return Audio
   */
  def insert(audio: Audio): Audio

  /**
   * @param audio Audio
   * @return Unit
   */
  def upgrade(audio: Audio): Unit

  /**
   * @param id Int
   * @return Unit
   */
  def ready(id: Int): Unit

  /**
   * @param id Int
   * @return Option[Audio]
   */
  def get(id: Int): Option[Audio]

  /**
   * @param id Int
   * @return List[Video]
   */
  def getByUserId(id: Int): List[Audio]

  /**
   * @param count Int
   * @return List[Audio]
   */
  def getAFew(count: Int): List[Audio]

  /**
   * @param id Int
   * @return Unit
   */
  def delete(id: Int): Unit
}

class AudioRepositoryImpl extends AudioRepository {
  import org.squeryl.PrimitiveTypeMode._
  val audios: Table[Audio] = FFlaySchema.audios

  /**
   * @param audio Audio
   * @return Audio
   */
  override def insert(audio: Audio): Audio = transaction(audios.insert(audio))

  /**
   * @param audio Audio
   * @return Unit
   */
  override def upgrade(audio: Audio): Unit = audios.update(audio)

  /**
   * @param id Int
   * @return Unit
   */
  override def ready(id: Int): Unit = update(audios)(a => where(a.id === id) set(a.ready := true))

  /**
   * @param id Int
   * @return Option[Audio]
   */
  override def get(id: Int): Option[Audio] = transaction {
    from(audios)(a => where(a.id === id) select a).headOption
  }

  /**
   * @param id Int
   * @return List[Video]
   */
  override def getByUserId(id: Int): List[Audio] = transaction {
    from(audios)(a => where(a.userId === id) select a).toList
  }

  /**
   * @param count Int
   * @return List[Audio]
   */
  override def getAFew(count: Int): List[Audio] = transaction {
    from(audios)(a => where(a.ready === true) select(a)).page(0, count).toList
  }

  /**
   * @param id Int
   * @return Unit
   */
  override def delete(id: Int): Unit = transaction(audios.deleteWhere(_.id === id))
}