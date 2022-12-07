package nl.bldn.aoc2022.puzzle07

import mu.KLogging
import nl.bldn.aoc2022.puzzle07.ElfCommands.CHOOSE_DIRECTORY
import nl.bldn.aoc2022.puzzle07.ElfCommands.LIST_DIRECTORY
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/puzzle/07")
class Puzzle07Resource {
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput(
        input: String,
    ): Int {
        val directories = input.parseTerminalOutput()

        val limit = 100000
        return directories.values.filter { it.computeSize() < limit }.sumOf { it.computeSize() }
    }

    @POST
    @Path("/part2")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    fun submitInput2(
        input: String,
    ): Int {
        val directories = input.parseTerminalOutput()

        val diskSize = 70000000
        val necessarySizeForUpdate = 30000000
        val rootSize = directories.getValue(ROOT_DIRECTORY).computeSize()
        val sizeToFreeUp = necessarySizeForUpdate - (diskSize - rootSize)

        return directories.values.filter { it.computeSize() > sizeToFreeUp }.sortedBy { it.computeSize() }.first().computeSize()
    }

    private fun String.parseTerminalOutput(): Map<String, ElfDirectory> {
        val knownDirectories = mutableMapOf<String, ElfDirectory>()
        knownDirectories.computeIfAbsent(ROOT_DIRECTORY) {  ElfDirectory() }

        var currentPath = mutableListOf(ROOT_DIRECTORY)

        for (line in this.lines()) {
            if (line.first() == '$') {
                // is command
                val matchEntire = commandPattern.matchEntire(line)
                val groups = matchEntire?.groupValues!!
                val command = ElfCommands.findByCommand(groups[1])

                when (command) {
                    CHOOSE_DIRECTORY -> {
                        val directoryName = groups[2].trim()
                        if (directoryName == ROOT_DIRECTORY) {
                            currentPath = mutableListOf(ROOT_DIRECTORY)
                        } else if (directoryName.startsWith(ROOT_DIRECTORY)) {
                            currentPath = directoryName.split("/").toMutableList()
                        } else if (directoryName == MOVE_UP) {
                            currentPath.removeLast()
                        } else {
                            val child = ElfDirectory(name = directoryName)
                            val parent = knownDirectories.getValue(currentPath.toPath())
                            parent.withNewDirectory(child)

                            currentPath.add(directoryName)
                            knownDirectories.computeIfAbsent(currentPath.toPath()) { child }
                        }
                    }
                    LIST_DIRECTORY -> {
                        // does nothing
                    }
                }

            } else {
                // is directory or file
                val elements = line.split(" ")
                if (elements[0] == "dir") {
//                    // is a directory - do nothing until you move into it
                } else {
                    val elfFile = ElfFile(nameAndExtension = elements[1], size = elements[0].toInt())
                    val directoryPath = currentPath.toPath()
                    val currentDirectory = knownDirectories.getValue(directoryPath)
                    currentDirectory.withNewFile(elfFile)
                }
            }
        }

        return knownDirectories
    }

    private fun List<String>.toPath() =
        if (size == 1) ROOT_DIRECTORY else "$ROOT_DIRECTORY${subList(1, size).joinToString("/")}"

    companion object : KLogging() {
        private const val MOVE_UP = ".."
        private const val ROOT_DIRECTORY = "/"
        private val commandPattern = Regex("\\$\\s(cd|ls)(\\s(.+))?")
    }
}

private enum class ElfCommands(val command: String) {
    CHOOSE_DIRECTORY("cd"),
    LIST_DIRECTORY("ls")
    ;

    companion object {
        fun findByCommand(s: String) = ElfCommands.values().singleOrNull { it.command == s } ?: throw IllegalArgumentException("Unknown command $s")
    }
}

private data class ElfDirectory(
    val name: String = "/",
    val files: List<ElfFile> = mutableListOf(),
    val directories: List<ElfDirectory> = mutableListOf(),
) {
    fun withNewFile(file: ElfFile) = (files as MutableList).add(file)
    fun withNewDirectory(directory: ElfDirectory) = (directories as MutableList).add(directory)

    fun computeSize(): Int {
        return files.sumOf { it.size } + directories.sumOf { it.computeSize() }
    }
}

private data class ElfFile(
    val nameAndExtension: String,
    val size: Int,
)