import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


case class DestFlightInfo(Destination: String, ArrDelay: Int)

object DestFlightInfo {
  def apply(line: String) = {
    val v = line.split(',')
    try {
      Some(new DestFlightInfo(v(17), v(14).toInt))
    } catch {
      case _ : Throwable => {
        println(v(17) + " " + v(14) + " Parse ERROR " + line); None
      }
    }
  }
}

object FlightInfo {
  def main(args: Array[String]) {
    val settings = new AppSettings()


    val inFile = "s3n://h2o-airlines-unpacked/year2008.csv"
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val flightData = sc.textFile(inFile).map(line => DestFlightInfo(line))
    val validFlightData = flightData.filter(_.isDefined).map(_.get)
    val filteredFlightData =
      if (settings.minDelay !=0)
        validFlightData.filter( f=> f.ArrDelay > settings.minDelay)
      else validFlightData
    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)

    import sqlContext.createSchemaRDD
    filteredFlightData.cache()
    filteredFlightData.count()
    validFlightData.registerTempTable("flights")

    val delayByCity = sqlContext.sql("SELECT Destination,percentile(ArrDelay,0.5) delay FROM flights group by Destination order by delay desc")

    delayByCity.collect.foreach(println(_))
  }

}