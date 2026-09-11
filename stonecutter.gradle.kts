plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.20.1"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["id"] = "\"${property("mod.id")}\";"
    swaps["name"] = "\"${property("mod.name")}\";"
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

}
