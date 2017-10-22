package io.lindhagen.piserver.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    22.10.2017
 */
@RestController
@RequestMapping("/api/v1/files")
class MediaFiles {

    @PostMapping("")
    fun uploadVideo(@RequestBody file: MultipartFile) {

        uploadFiles(listOf(file))
    }


    private fun uploadFiles(files: List<MultipartFile>) {
        files.forEach({
           if (!it.isEmpty) {

               val bytes = it.bytes
               val path = Paths.get(
                       "resources/media",
                       it.originalFilename)
               Files.write(path, bytes)
           }
        });
    }

}