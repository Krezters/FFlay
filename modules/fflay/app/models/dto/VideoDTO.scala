package models.dto

import models.dao.entities.Video

case class VideoDTO(
  name: String,
  link: String,
  userId: Int,
  data: String,
  id: Int = 0,
  ready: Boolean = false
) {
  /**
   * @return models.dao.entities.Video
   */
  def toModel: Video = Video(id, name, link, ready, userId)

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
            ): VideoDTO = VideoDTO(name, link, userId, data, id, ready)
}
