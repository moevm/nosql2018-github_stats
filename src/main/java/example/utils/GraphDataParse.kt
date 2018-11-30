package example.utils

import example.model.mongo.Contributor
import example.model.mongo.Course
import org.joda.time.DateTime
import java.util.*

object GraphDataParse {

    fun parseCourseToData(course: Course): GraphInfo {
        val topContributors = course.repositories.map { it.contributors }.flatten().sortedByDescending { it.commits.size }.take(5)
        return GraphInfo(
                circle = SimpleGraphInfo(
                        dataset = course.repositories.map { it.contributors.map { it.commits.size }.sum() },
                        labels = course.repositories.map { it.name }
                ),
                top = SimpleGraphInfo(
                        dataset = topContributors.map { it.commits.size },
                        labels = topContributors.map { it.name }
                ),
                timeline = TimelineGraphInfo(
                        dataset = course.repositories
                                .map { it.contributors.map { it.commits.map { it.date } }.flatten() to it.name }
                                .map { ChartInfo(parseTimelineInfo(it.first), it.second) }
                )
        )
    }

    fun parseRepositoryToData(contributors: List<Contributor>): GraphInfo {
        val topContributors = contributors.sortedByDescending { it.commits.size }.take(5)
        return GraphInfo(
                circle = SimpleGraphInfo(
                        dataset = contributors.map { it.commits.size },
                        labels = contributors.map { it.name }
                ),
                top = SimpleGraphInfo(
                        dataset = topContributors.map { it.commits.size },
                        labels = topContributors.map { it.name }
                ),
                timeline = TimelineGraphInfo(
                        dataset = contributors
                                .map { it.commits.map { it.date } to it.name }
                                .map { ChartInfo(parseTimelineInfo(it.first), it.second) }
                )
        )
    }

    private fun parseTimelineInfo(items: List<Date>): List<Point> = items
            .map { DateTime(it) }
            .groupBy { it.withTimeAtStartOfDay() }
            .map { Point(it.key.millis, it.value.size) }
            .sortedBy { it.x }

}

data class GraphInfo(val circle: SimpleGraphInfo, val top: SimpleGraphInfo, val timeline: TimelineGraphInfo)

data class SimpleGraphInfo(val dataset: List<Int>, val labels: List<String>)

data class TimelineGraphInfo(val dataset: List<ChartInfo>)

data class Point(val x: Long, val y: Int)

data class ChartInfo(val points: List<Point>, val label: String)
