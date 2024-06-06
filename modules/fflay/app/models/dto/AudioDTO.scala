package models.dto

import models.dao.entities.Audio

case class AudioDTO(
  name: String,
  link: String,
  userId: Int,
  data: String,
  id: Int = 0,
  ready: Boolean = false
) {
  /**
   * @return models.dao.entities.Audio
   */
  def toModel: Audio = Audio(id, name, link, ready, userId)

  /** Updating DTO Fields
   * @param name String
   * @param link String
   * @param userId Int
   * @param data String
   * @param ready Boolean
   * @return
   */
  def update(name: String = name,
             link: String = link,
             userId: Int = userId,
             data: String = data,
             ready: Boolean = ready
            ): AudioDTO = AudioDTO(name, link, userId, data, id, ready)
}
