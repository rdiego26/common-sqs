package com.taxis99.amazon.streams

import akka.stream.scaladsl.{Sink, Source}
import com.amazonaws.services.sqs.model.Message
import play.api.libs.json.Json
import test.StreamSpec

class SerializerSpec extends StreamSpec {

  case class MyCustomType(s: String, i: Int, f: Double, b: Boolean, m: Map[String, String], l: Seq[Int])

  object MyCustomType {
    implicit val myCustomTypeFormat = Json.format[MyCustomType]
  }

  val msg = MyCustomType("foo", 1, 1.0, false, Map("1" -> "2"), Seq(0 , 1))
  val jsonMsg = Json.toJson(msg)

  "#econde" should "return the string representation of the JsValue" in {
    Source.single(jsonMsg) via Serializer.encode runWith Sink.head map { result =>
      result shouldBe Serializer.pack(jsonMsg)
    }
  }

  "#decode" should "return the JsValue from a Amazon SQS message" in {
    val sqsMsg = new Message()
    sqsMsg.setBody(Serializer.pack(jsonMsg))

    Source.single(sqsMsg) via Serializer.decode runWith Sink.head map { result =>
      result shouldBe jsonMsg
    }
  }
}
