package com.example.todaysaying

import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
        private val quotes : List<Quote>,    //아이템리스트
        private val isNameRevealed : Boolean
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {//뷰페이저2같은경우 리사이클러뷰를 기반
//RecyclerView.Adapter<>상속받아야함 //제네릭 타입 안에 뷰 홀더를 넣어주어야함.
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){ //내부클래스로 뷰홀더 클래스 생성 :RecyclerView상속받아야함 //생성자에서 받은 itemView를 뷰홀더(부모클래스)에 넘겨줌

    //Quote를 어떻게 랜더링할지
    private val quoteTextView : TextView = itemView.findViewById(R.id.quoteTextView)
    private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
    //바인드메소드 구현
        fun bind(quote : Quote, isNameRevealed: Boolean){
            quoteTextView.text = quote.quote
            if(isNameRevealed){
                nameTextView.text = quote.name
                nameTextView.visibility = View.VISIBLE
            }else{
                nameTextView.visibility = View.GONE
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            //뷰홀더안에 인플레이트하여 만든 뷰를 넣어줌
            // onBindViewHolder가 호출이되면 세팅이 된 아이들을 불러서 넣어줌
            QuoteViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_quote,parent,false)
            )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {     //어떤것을 바인드 할것인가
        holder.bind(quotes[position], isNameRevealed)
    }

    override fun getItemCount() = quotes.size
}