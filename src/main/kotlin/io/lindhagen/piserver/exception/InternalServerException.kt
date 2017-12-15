package io.lindhagen.piserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    12/11/2017
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class InternalServerException(err: String) : RuntimeException(err)