# scala-zio2-scopt-monocle-workshop

* references
    * https://github.com/scopt/scopt
    * https://www.baeldung.com/scala/read-command-line-arguments
    * https://github.com/optics-dev/Monocle
    * https://www.optics.dev/Monocle/

## preface
* goals of this workshop
    * introduction into command line applications using scopt
* workshop
    * implement command to print random number that can be long or int and positive or negative
        * example: `random --positive`
    * run using `docker run`
        * example `docker run scala-zio2-scopt-workshop sum -c1 2 -c2 3`
        * remember to first `docker:publishLocal`

## scopt
* command line options parsing library
* functional DSL
    * scaffold
        ```
        import scopt.OParser
        val builder = OParser.builder[Config]
        val parser1 = {
          import builder._
          OParser.sequence(
            programName("scopt"),
            head("scopt", "4.x"),
            // command or options,
            // validations
          )
        }
        ```
    * option
        * flag
            ```
            opt[Unit]("verbose")
                .action((_, c) => c.copy(verbose = true))
                .text("verbose is a flag")
            ```
        * typed option
            ```
            opt[Int]("foo")
                .abbr("f")
                .action((x, c) => c.copy(foo = x))
                .text("foo is an integer property"),
            ```
        * preconfigured
            ```
            help("help") // prints usage text
            version("version") // prints header text
            note("...") // add given string to the usage text
            ```
            * by default, when the --help or --version are invoked, they call sys.exit(0) after printing the
            help or version information
        * arguments
            * similar to options, but accepts values without `--` or `-`
            * example: "sum 2 3"
            * prefer options
            * example
                ```
                cmd("sum")
                    .action((_, _) => Command.Sum(0, 0))
                    .text("sum(a, b) = a + b")
                    .children(
                      arg[Int]("constituent1")
                        .abbr("c1")
                        .action((value, command) => command.asInstanceOf[Sum].copy(component1 = value))
                        .text("constituent1 is an Int property"),
                      arg[Int]("constituent2")
                        .abbr("c2")
                        .action((value, command) => command.asInstanceOf[Sum].copy(component2 = value))
                        .text("constituent2 is an Int property"),
                    )
                ```
        * validation
            ```
            opt[Int]('f', "foo")
              .action( (x, c) => c.copy(intValue = x) )
              .validate( x =>
                if (x > 0) success
                else failure("Option --foo must be >0") )
              .validate( x => failure("Just because") )
            ```
    * command
        * used to express git branch kind of argument (name means something)
        * example
            ```
            cmd("update")
                  .action((_, c) => c.copy(mode = "update"))
                  .text("update is a command.")
                  .children(
                    // options
                  )
            ```
    * validation
        ```
        checkConfig(
          c =>
            if (c.keepalive && c.xyz) failure("xyz cannot keep alive")
            else success)
        )
        ```
* parsing
    * basic
        ```
        OParser.parse(parser1, args, Config()) match {
          case Some(config) =>
            // do something
          case _ =>
            // arguments are bad, error message will have been displayed
        }
        ```
        uses as a default
        ```
        abstract class DefaultOEffectSetup extends OEffectSetup {
          override def displayToOut(msg: String): Unit = {
            Console.out.println(msg)
          }
          override def displayToErr(msg: String): Unit = {
            Console.err.println(msg)
          }
          override def reportError(msg: String): Unit = {
            displayToErr("Error: " + msg)
          }
          override def reportWarning(msg: String): Unit = {
            displayToErr("Warning: " + msg)
          }
          override def terminate(exitState: Either[String, Unit]): Unit =
            exitState match {
              case Left(_)  => platform.exit(1)
              case Right(_) => platform.exit(0)
            }
        }
        ```
    * advanced
        * by default, scopt emits output when needed to stderr and stdout
        * if your application requires parsing arguments while not producing output directly, you may wish to
        intercept the side effects
        * example
            ```
            OParser.runParser(parser1, args, Config()) match {
              case (result, effects) =>
                OParser.runEffects(effects, new DefaultOEffectSetup {
                    // override methods here
                })

                result match {
                  Some(config) =>
                    // do something
                  case _ =>
                    // arguments are bad, error message will have been displayed
                }
            }
            ```
* custom types
    * example
        ```
        object WeekDays extends Enumeration {
          type WeekDays = Value
          val Mon, Tue, Wed, Thur, Fri, Sat, Sun = Value
        }

        implicit val weekDaysRead: scopt.Read[WeekDays.Value] =
          scopt.Read.reads(WeekDays withName _)
        ```
    * refined types
        ```
        implicit val nonZeroRead: scopt.Read[NonZeroInt] =
          scopt.Read.intRead.map(unsafeParseInt)

        private def unsafeParseInt(i: Int): NonZeroInt = refineV[NonZero](i)
          .fold(_ => throw new IllegalArgumentException("'" + i + "' cannot be zero."), identity)
        ```