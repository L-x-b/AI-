import {AtButton} from "taro-ui";
import {View, Image} from '@tarojs/components'
import Taro from "@tarojs/taro";
import "taro-ui/dist/style/components/button.scss" // 按需引入

import './index.scss'
import headerBg from '../../assets/headerBg.jpg';

export default () => {
  return (
    <View className='indexPage'>
      <View className='at-article__h1 title'>MBTI 性格测试</View>
      <View className='at-article__h2 subTitle'>只需2分钟，就能非常准确地描述出你是谁，以及你得性格特点</View>
      <AtButton
        type='primary'
        size='normal'
        className='enterBtn'
        circle
        onClick={() => {
          Taro.navigateTo({
            url: '/pages/doQuestionPage/index'
          })
        }}
      >
        开始测试
      </AtButton>
      <Image src={headerBg} style={{width: '100%'}} mode='aspectFill' />

    </View>
  )
}
