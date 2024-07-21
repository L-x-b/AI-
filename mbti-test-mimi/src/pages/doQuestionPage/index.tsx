import Taro from "@tarojs/taro";
import {AtButton, AtRadio} from "taro-ui";
import {View} from '@tarojs/components'
import {useEffect, useState} from "react";
import './index.scss'
import question from '../../data/question.json'
import GlobalFooter from "../../components/GlobalFooter";

export default () => {

  // 定义当前题目序号
  const [current, setCurrent] = useState<number>(1);
  // 定义当前题目
  const [currentQuestion, setCurrentQuestion] = useState<any>(question[0]);
  const radioOptions = currentQuestion.options.map((option) => {
    return {
      label: `${option.key}.${option.value}`,
      value: option.key
    };
  });
  // 当前回答
  const [currentAnswer, setCurrentAnswer] = useState<string>();
  // 回答列表
  const [answerList] = useState<string[]>([]);

  // 序号变化时，切换当前题目和当前回答
  useEffect(() => {
      setCurrentQuestion(question[current - 1]);
      setCurrentAnswer(answerList[current - 1]);
    }
    , [current]);

  return (
    <View className='doQuestionPage'>
      <View className='at-article__h2 title'>
        {current}、{currentQuestion.title}
      </View>
      <View className='options-wrapper'>
        <AtRadio
          options={radioOptions}
          value={currentAnswer}
          onClick={(value) => {
            setCurrentAnswer(value);
            // 回答记录
            answerList[current - 1] = value;
          }}
        />
      </View>

      {current > 1 && (
        <AtButton
          size='normal'
          className='controlBtn'
          circle
          onClick={() => {
            setCurrent(current - 1);
          }}
        >
          上一题
        </AtButton>
      )}

      {current < question.length && (
        <AtButton
          type='primary'
          size='normal'
          className='controlBtn'
          circle
          disabled={!currentAnswer}
          onClick={() => {
            setCurrent(current + 1);
          }}
        >
          下一题
        </AtButton>
      )}

      {current == question.length && (
        <AtButton
          type='primary'
          size='normal'
          className='controlBtn'
          circle
          disabled={!currentAnswer}
          onClick={() => {
            Taro.setStorageSync('answerList', answerList);
            Taro.navigateTo({
              url: '/pages/resultPage/index'
            })
          }}
        >
          查看结果
        </AtButton>
      )}

      <GlobalFooter />

    </View>
  )

}
