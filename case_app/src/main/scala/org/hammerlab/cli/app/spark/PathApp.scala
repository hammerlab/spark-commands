package org.hammerlab.cli.app.spark

import org.hammerlab.cli.app.close.Closeable
import org.hammerlab.cli.app.spark.Registrar.noop
import org.hammerlab.cli.app.{ Args, ArgsOutPathApp, HasPrintLimit }
import org.hammerlab.spark.confs

/**
 * [[HasSparkContext]] that takes an input path and prints some information to stdout or a path, with optional truncation of
 * such output.
 */
abstract class PathApp[Opts](_args: Args[Opts],
                             reg: Registrar = noop)(
    implicit c: Closeable
)
  extends ArgsOutPathApp[Opts](_args)
    with HasSparkContext
    with HasPrintLimit
    with confs.Kryo {
  reg.apply(this)
}
