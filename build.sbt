name := "spark-commands"
version := "1.0.4-SNAPSHOT"

providedDeps += libs.value('spark)

deps ++= Seq(
  libs.value('bdg_utils_cli),
  libs.value('slf4j)
)
