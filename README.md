### TodaySaying

### [오늘의 명언] todaySaying   
### [Firebase_Remote Config] 를 활용하여 실제로 코드수정이나 별도의 배포없이 앱의 내용을 변경 가능.   

### [기능상세]   
	->코드수정 없이 명언을 추가할 수 있다.   
	->코드 수정 없이 이름을 숨길 수 있다.   
	->무한 스와이프 할 수 있다.
### [활용 기술]    
	->Firebase Remote Config   
	->ViewPager2   

### 00.프로젝트 셋업   
### 01.기본 UI구성  
	ViewPager2   
		->ViewPager에서 개선된버전(orientation서포트.세로가능, RTL서포트, NotifydatasetChanged개선, 리사이클러뷰 기반으로 작업)
    
   ### 리사이클러뷰에 익숙해지기
   리스트를 그리는 방법
	
리스트뷰 = 유사한 뷰가 반복
1.AddView (실제로 리스트뷰를 그리기위해서 잘 사용되지 않는다.)
	-> 뷰를 담을 LinearLayout컨테이너 만들어줌
	-> Item을 담을 XML을 만들어준다(목록에 들어갈 뷰).
	-> 클래스만듬(ex자동차클래스) 아이템리스트 준비
	-> 그 XML에 내용을 채워준다 
		->inflater사용(LayoutInflater.from(지금 액티비티)) 현재 액티비티에서 가져옴(그려주는 곳)
		->inflater.inflate(아이템뷰XML layout, null, )
		-> Container View에 더해준다 -> 반복한다.
		
			val container = 컨테이너 레이아웃
			val inflater = LayoutInflater.from(지금 액티비티) //그리는곳

			val itemView = inflater.inflate(R.layout.item_view, null) //아이템뷰에서 가져옴(그려줄 아이템)
			val carNameView = itemView.findViewById<TextView>(R.id.car_name)
			val carEngineView = itemView.findViewById<~	
			반복문 사용	
			carNameView.setText()~~ 내용을 채워줌
			container.addView(itemView) //(그려줄 아이템뷰)추가 해줌

addView를 하면 스크롤이 자동장착 안 됨 -> 스크롤뷰로 감싸줌.
		
2.ListView (예전에 많이 사용)
	
	->리스트로 만들고 싶은 아이템의 리스트를 준비
	(어답터를 이용한다.){  //중요
		->인플레이터를 준비한다.
		->인플레이터로 아이템 하나에 해당하는 뷰를 만들어준다.
		->위에서 만든 뷰를 컨테이너 뷰에 붙여준다.
	}
	->만든 어답터 클래스 넣어주기
	->리스트 뷰에 리스너 장착
	
oncreate...

	val adapter = ListViewAdapter(carList, LayoutInflater.from(this@지금 액티비티))
	listView.adapter = adapter	//만든 어답터 클래스 넣어주기
	listView.setOnItemClickLister(){->	 //리스트 뷰에 리스너 장착
		val carName = (adapter.getItem(position) as CarForList).name
		Toast.makeText(this,"리스너 누름",Toast.LENGTHLONG).show()
	}
		->XML에 ListView리스트뷰 추가
		->어답터 만들기(리스트뷰 어답터 Class 생성) ->BaseAdapter()상속
			class ListViewAdapter( val carForList : ArrayList<>, val layoutInflater : LayoutInflater) : >BaseAdapter(){ //인플레이터 가져오기
				->getView(position,convertView,parent) 오버라이드	//
					
					->val view = layoutInflater.inflate(R.layout.그려줄아이템 laytout, null)	
					->var carNameTextview = view.findViewById(R.id.car_name)
					->var carEngineTextview = view.findViewById(R.id.car_engine)

					->carEngineTextview.setText(carForList.get(position).name)
					->carEngineTextview.setText()~~
					return view

				->getItem(position)	 //해당 인덱스 아이템 하나 리턴
					->carForList.get(position)
				->getItemId(position)	//ID를 해당 아이템 포지션으로 부여	
					->return position.toLong() // 그냥 포지션 리턴
				->getCount(position) 	//아이템 목록 개수 알려주기
					->return CarForList.size  


			

AddView와 ListView의 차이점
1. 만드는 방식이 다르다.
2. 그리는 방식이 다르다.
	AddView -> 리스트 갯수와 상관없이 한 번에 다그린다.
	ListView -> 보여지는 부분 + 알파만 한 번에 그리고 필요한 경우에 더 그린다. 

리스트 뷰 사용시 [뷰 홀더]를 사용하여 무겁고 리소스를 많이 사용하는 findViewById()개선
	-> 뷰홀더 클래스 만들기 
	
	class ViewHolder{ //뷰홀더
		var carName: TextView? = null
		var carEngine : TextView? = null
	}
	->getView(position,convertView,parent)메소드 기존 내용 지우고 
		override fun getView(position,convertView,parent): View{ 	//convertView:도는 뷰
			val view : View
			val holder : ViewHolder
			
			if(converView == null){ //재사용할 뷰가 없다면
				view = layoutInflater.inflate(R.layout.그려줄아이템, null)	//인플레이트 해주고
				holder = ViewHolder() //만들어준 뷰홀더 설정
				holder.carName = view.findViewById(R.id.car_name)	//만들어둔 뷰를 찾은 후 홀더에 집어넣음
				holder.carEngine = view.findViewById(R.id.car_Engine)

				view.tag = holder //찾을 수 있도록 태그를 달아둠 
			}else{	//재사용 할 것이 있다면//컨버터가 있다면
				holder = convertView.tag as ViewHolder //태그를 보고 가져와서 뷰 홀더로 바꿔줌
				view  = convertView //뷰에 똑같은 애가 재활용됨 //convert뷰를 뷰홀더로 바꿔주고 내용물만 다시 채워줌
			}
			holder.carName?.setText(carForList.get(position).name) 	//뷰를 넣어줌
			holder.carEngine?.setText(carForList.get(position).engine) 	
			return view
		}

3.RecyclerView -> 최근에 가장 많이 사용이 되고 있고 가장 효율이 높다.
-장점
	->ListView의 개선판
		->ViewHolder포함하고있기때문
	->유연하다
		-> LayoutManager		//스크롤 수평방향도 가능
			->Linear
			->Glide
			->StaggeredGrid

액티비티 RecyclerViewActivity

->build.gradle 앱단위에서 '리사이클러뷰' 추가
->레이아웃 XML에 리사이클러뷰 태그 추가 <~rycyclerview.~>
->어답터 생성(class RecyclerViewAdapter)

	class RecyclerViewAdapter(
		val itemList : ArrayList<carForList>,	//아이템리스트 인자로 받음
		val inflater : LayoutInflater	//인플레이터를 인자로 받음
	): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{	//상속받아야함 //제네릭 타입 안에 뷰 홀더를 넣어주어야함.

		class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){ //내부클래스로 뷰홀더 클래스 생성 :RecyclerView상속받아야함	//생성자에서 받은 itemView를 뷰홀더(부모클래스)에 넘겨줌
			val carName : TextView
			val carEngine : TextView
			init{ //어답터가 만들어지자마자 carname과 carengine생성 	//초기화블럭
				carName = itemView.findViewById(R.id.car_name)
				carEngine = itemview.findViewById(R.id.car_Engine)
			}
		}
		//메소드 세개구현해줌
		override fun onCreateViewHolder(parent : ViewGroup, viewType :Int): ViewHolder { 	//뷰를 만들어줌
			val view = inflater.inflate(R.layout.그려줄아이템뷰,parent, false)	//인플레이트로 아이템하나 뷰를 만듬 
			return ViewHolder(view)	//낯선부분 -> 뷰홀더안에 위에서 만든 뷰를 넣어줌 ->view는 위의 뷰홀더(itemView)인자에 들어간것 ->onBindViewHolder가 호출이되면 세팅이 된 아이들을 불러서 넣어줌
		}
		override fun getItemCount() : Int{
			return itemList.size
		}
		override fun onBindViewHolder(holder : ViewHolder, position : Int){	//리스트뷰로 친다면 뷰홀더를 만들어주고 태그를 달아서 다시 찾아서 재활용 //뷰를 그려줌
			holder.carName.setText(itemList.get(position).name)	//어떤것을 바인드 할것인가
			holder.carEngine.setText(itemList.get(position).engine)
		}

->oncreate에서

	val adapter = RecyclerViewAdapter(carList, LayoutInflater.from(this@지금액티비티))
	recycler_view.adapter = adapter //xml에 있는 리사이클러뷰에 어답터 추가
	recycler_view.layoutManager = LinearLayoutManager(this@지금액티비티) //레이아웃매니저 사용 //GridLayoutManager()도 사용가능 //

->뷰홀더에 클릭리스너 달아주기 좋음 . 위의 뷰홀더의init블럭

			init{ 
				carName = itemView.findViewById(R.id.car_name)
				carEngine = itemview.findViewById(R.id.car_Engine)
				itemView.setOnClickListner{//몇번째에 클릭을 했는지 포지션 필요 -> adapterPosition 사용
					val position : Int = adapterPosition
					val engineName = itemList.get(position).engine
				}
			}
inner class를 써줌으로서 외부클래스 멤버변수에 접근 가능해짐
    
	
	build.gradle app단위에 material안에 이미 포함되어있음. 별도의 dependencies 선언 필요 X	   
	레이아웃에 뷰페이저2 추가(XML) -> 어댑터 추가해서 실제로 랜더링하는 부분 추가(랜더링하는 아이템xml추가) //뷰홀   더??   
			두가지 데이터 필요 -> 하나의 모델로 정의(데이터 클래스 추가)  
			->페이저 어답터 추가(클래스 추가),  RecyclerView.Adapter상속    
			->제네릭 안에 뷰홀더넣어야함. 내부클래스로 정의     
			->메소드 세개 구현      
			->활용할 데이터 추가해줌( 어댑터 클래스 인자추가)   
	->mainActivity에 있는 ViewPager와 연동 -> 뷰페이저어댑터를 설정   
### 02.Remote Config 구성   
	https://firebase.google.com/docs/remote-config
	앱 업데이트를 게시하지 않아도 하루 활성 사용자 수 제한 없이 무료로 앱의 동작과 모양을 변경할 수 있습니다.
	![param-precedence](https://user-images.githubusercontent.com/68258365/132102736-0fb8dd94-d3d5-4f4e-88cd-b9bead82afa0.png)
	

### 03.Remote Config 연동   
