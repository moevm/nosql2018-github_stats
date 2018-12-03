package example.utils

import example.constants.ItemType
import example.model.mongo.Contributor
import example.model.mongo.Course
import example.model.mongo.abstractEntity.AnalyzedEntity

object GraphDataParse {

    private const val POINTS_DIVIDER = 1

    fun parseCourseToData(course: Course, type: ItemType): GraphInfo {
        val topContributors = course.repositories
                .map { it.contributors }
                .flatten()
                .sortedByDescending { it.getItems(type).size }
                .take(5)
        return GraphInfo(
                circle = SimpleGraphInfo(
                        dataset = course.repositories.map { it.contributors.map { it.getItems(type).size }.sum() },
                        labels = course.repositories.map { it.name }
                ),
                top = SimpleGraphInfo(
                        dataset = topContributors.map { it.getItems(type).size },
                        labels = topContributors.map { it.name }
                ),
                timeline = parsePoints(course.repositories.map { it.name to it.contributors.map { it.getItems(type) }.flatten() })
        )
    }

    fun parseRepositoryToData(contributors: List<Contributor>, type: ItemType): GraphInfo {
        val topContributors = contributors
                .sortedByDescending { it.getItems(type).size }
                .take(5)
        return GraphInfo(
                circle = SimpleGraphInfo(
                        dataset = contributors.map { it.getItems(type).size },
                        labels = contributors.map { it.name }
                ),
                top = SimpleGraphInfo(
                        dataset = topContributors.map { it.getItems(type).size },
                        labels = topContributors.map { it.name }
                ),
                timeline = parsePoints(contributors.map { it.name to it.getItems(type) })
        )
    }

    fun parseContributorToData(contributor: Contributor, type: ItemType): GraphInfo = GraphInfo(
            null,
            null,
            parsePoints(listOf(contributor.name to contributor.getItems(type)))
    )

    private fun parsePoints(groups: List<Pair<String, List<AnalyzedEntity>>>): TimelineGraphInfo {
        val allItems = groups.map { it.second }.flatten().map { it.date.time }
        if (allItems.isEmpty()) {
            return TimelineGraphInfo(groups.map { ChartInfo(listOf(), it.first) })
        }
        val min = allItems.min()!!
        val max = allItems.max()!!
        val points = allItems.size / groups.size / POINTS_DIVIDER
        val diff = (max - min) / points
        val listOfPoints = mutableListOf(min)
        for (i in (0 until points - 1)) {
            listOfPoints.add(listOfPoints.last() + diff)
        }
        val listOfMidPoints = listOfPoints.map { it + diff / 2 }
        return TimelineGraphInfo(groups.map { (label, points) ->
            ChartInfo(
                    points = listOfMidPoints.zip(countGroups(points.map { it.date.time }, listOfPoints), ::Point),
                    label = label
            )
        })

    }

    private fun countGroups(items: List<Long>, points: List<Long>): List<Int> {
        val counts = points.map { 0 }.toMutableList()
        items.forEach { item ->
            points.forEachIndexed { index, point ->
                if (index == points.size - 1) {
                    if (item < point) {
                        counts[index - 1] = counts[index - 1] + 1
                    } else {
                        counts[index] = counts[index] + 1
                    }
                } else if (item < point) {
                    counts[index - 1] = counts[index - 1] + 1
                    return@forEach
                }
            }
        }
        return counts
    }

}

data class GraphInfo(val circle: SimpleGraphInfo?, val top: SimpleGraphInfo?, val timeline: TimelineGraphInfo)

data class SimpleGraphInfo(val dataset: List<Int>, val labels: List<String>)

data class TimelineGraphInfo(val dataset: List<ChartInfo>)

data class Point(val x: Long, val y: Int)

data class ChartInfo(val points: List<Point>, val label: String)
