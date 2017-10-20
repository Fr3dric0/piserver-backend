package io.lindhagen.piserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException: RuntimeException {

    constructor(err: String) : super(err)
}