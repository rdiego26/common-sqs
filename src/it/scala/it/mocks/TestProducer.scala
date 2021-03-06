package it.mocks

import com.taxis99.amazon.serializers.{ISerializer, MsgPack}
import com.taxis99.amazon.sqs.{SqsClient, SqsProducer}

import scala.concurrent.ExecutionContext

class TestProducer(queueName: String)
                  (implicit val ec: ExecutionContext, val sqs: SqsClient) extends SqsProducer[TestType] {

  override def serializer: ISerializer = MsgPack

  def queue = queueName
}
