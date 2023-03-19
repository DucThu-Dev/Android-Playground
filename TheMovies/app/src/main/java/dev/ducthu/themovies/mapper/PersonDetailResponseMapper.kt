package dev.ducthu.themovies.mapper

import dev.ducthu.themovies.model.network.PersonDetail

class PersonDetailResponseMapper : NetworkResponseMapper<PersonDetail> {
    override fun onLastPage(response: PersonDetail): Boolean {
        return true
    }
}