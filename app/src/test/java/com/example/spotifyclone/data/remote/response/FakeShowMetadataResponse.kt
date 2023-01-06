package com.example.spotifyclone.data.remote.response

val fakeShowMetadataResponse = ShowMetadataResponse(
    id = "fakeShowMetadataResponse",
    name =  "Fake Show",
    publisher = "Fake Publisher",
    images = List(3) { fakeImageResponse }
)