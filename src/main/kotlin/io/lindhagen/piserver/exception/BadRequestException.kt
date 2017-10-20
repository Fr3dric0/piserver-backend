package io.lindhagen.piserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequestException: RuntimeException {

    constructor(err: String) : super(err)
}