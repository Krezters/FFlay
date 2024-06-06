package module

import di.AppModule
import models.dao.repositories.{AudioRepository, AudioRepositoryImpl, UserRepository, UserRepositoryImpl, VideoRepository, VideoRepositoryImpl}
import models.services.{AudioService, AudioServiceImpl, FFmpegService, FFmpegServiceImpl, FileService, FileServiceImpl, LogService, LogServiceImpl, UserService, UserServiceImpl, VideoService, VideoServiceImpl}

class MainModule extends AppModule{
  override def configure(): Unit = {
    bindSingleton[UserRepository, UserRepositoryImpl]
    bindSingleton[VideoRepository, VideoRepositoryImpl]
    bindSingleton[AudioRepository, AudioRepositoryImpl]

    bindSingleton[LogService, LogServiceImpl]
    bindSingleton[UserService, UserServiceImpl]
    bindSingleton[VideoService, VideoServiceImpl]
    bindSingleton[AudioService, AudioServiceImpl]
    bindSingleton[FileService, FileServiceImpl]
    bindSingleton[FFmpegService, FFmpegServiceImpl]
  }
}
