import Taro from "@tarojs/taro";
import {AtButton} from "taro-ui";
import {Image, View} from '@tarojs/components'
import './index.scss'
import {getBestQuestionResult} from "../../utils/bizUtils";
import question from '../../data/question.json'
import questionResults from '../../data/question_results.json'
import headerBg from "../../assets/headerBg.jpg";
import GlobalFooter from "../../components/GlobalFooter";


export default () => {
  const answerList = ["B", "B", "B", "A", "B", "B", "B", "A", "B", "B"]
  // const answerList = Taro.getStorageSync('answerList');
  if (!answerList || answerList.length < 1) {
    Taro.showToast({
      title: '答案为空',
      icon: 'error',
      duration: 3000
    })
  }
  const result = getBestQuestionResult(answerList, question, questionResults);

  return (
    <View className='resultPage'>
      <View className='at-article__h1 title'>{result.resultName}</View>
      <View className='at-article__h2 subTitle'>{result.resultDesc}</View>
      <AtButton
        type='primary'
        circle
        className='enterBtn'
        onClick={() => {
          Taro.reLaunch({
            url: '/pages/indexPage/index',
          });
        }}
      >
        返回主页
      </AtButton>
      <Image className='headerBg' style={{width: '100%'}} src={headerBg} mode='aspectFill'/>

    </View>
  )

}
