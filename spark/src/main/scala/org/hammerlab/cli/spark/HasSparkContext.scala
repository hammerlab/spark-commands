package org.hammerlab.cli.spark

import grizzled.slf4j.Logging
import hammerlab.cli
import hammerlab.path._
import org.apache.spark.SparkContext
import org.hammerlab.hadoop.Configuration
import org.hammerlab.spark.{ SparkConfBase, confs }

trait HasSparkConf
  extends SparkConfBase
    with confs.Kryo
    with confs.DynamicAllocation
    with confs.EventLog
    with confs.Speculation

trait HasSparkContext
  extends HasSparkConf
    with Logging {

  self: cli.App[_] ⇒

  @transient private var _sc: SparkContext = _

  implicit def sc: SparkContext = {
    if (_sc == null) {
      info("Creating SparkContext")
      val conf = makeSparkConf
      if (
          conf.get("spark.eventLog.enabled", "true") == "true" &&
          conf.get("spark.eventLog.dir", "") == "" &&
          !Path("/tmp/spark-events").exists) {
        conf.set("spark.eventLog.enabled", "false")
        warn("Disabling event-logging because default destination /tmp/spark-events doesn't exist")
      }

      _sc = new SparkContext(conf)
    }
    _sc
  }

  implicit def conf: Configuration = sc.hadoopConfiguration

  deinit {
    if (_sc != null && !_sc.isStopped) {
      info("Stopping SparkContext")
      _sc.stop()
    }
    _sc = null
  }
}
