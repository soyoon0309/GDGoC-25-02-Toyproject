// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest('DELETE',`/api/articles/${id}`, null, success, fail);
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        })

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`);
        }

        httpRequest('PUT',`/api/articles/${id}`, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    createButton.addEventListener('click', event => {
        // formData 생성
        const formData = new FormData();

        // articleData 생성 (JSON data)
        const articleData = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };

        // JSON을 Blob으로 변환하여 'request' 파트에 추가
        // Controller의 @RequestPart("request")가 이를 인식함
        formData.append('request', new Blob([JSON.stringify(articleData)], {
            type: 'application/json'
        }));

        // image file data 추가
        const imgInput = document.getElementById('files');
        if (imgInput && imgInput.files) {
            // Controller의 @RequestPart("images")가 이를 인식함
            for (let i = 0; i < imgInput.files.length; i++) {
                formData.append('images', imgInput.files[i]);
            }
        }

        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles');
        };

        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles');
        };

        httpRequest('POST','/api/articles', formData, success, fail)
    });
}
//좋아요 기능
const likebutton=document.getElementById("like-btn");
if(likebutton){
    likebutton.addEventListener('click',likeArticle);
}
function likeArticle(){
    const articleId=document.getElementById('article-id').value;
    const url=`/api/articles/${articleId}/like`;

    // 현재 좋아요 개수와 아이콘 요소
    const likeCountSpanElement = document.getElementById('like-count');
    const likeCountElement = likeCountSpanElement.querySelector('span'); // 개수 숫자 자체
    const likeIconElement = document.getElementById('like-icon'); // 하트 아이콘

    function success(response) {
        // 서버에서 반환된 boolean 값 (true: 좋아요 됨, false: 좋아요 취소)을 JSON 파싱해야 함
        // *주의*: httpRequest 함수가 response.json()을 바로 반환하도록 수정해야 함 (아래 참고)
        // 여기서는 response가 이미 파싱된 JSON 객체라고 가정하고 로직 작성

        let isLiked = response; // response 자체가 boolean 값이라고 가정
        let currentCount = parseInt(likeCountElement.textContent, 10);

        if (isLiked) {
            // 좋아요 추가
            likeCountElement.textContent = currentCount + 1;
            likeIconElement.textContent = '❤️'; // 채워진 하트
            alert('좋아요 완료!');
        } else {
            // 좋아요 취소
            likeCountElement.textContent = currentCount - 1;
            likeIconElement.textContent = '🤍'; // 비워진 하트
            alert('좋아요 취소!');
        }
    }

    function fail(status) {
        if (status === 401) {
            alert('로그인이 필요합니다.');
        } else {
            alert('좋아요 처리 중 오류가 발생했습니다.');
        }
    }

    // 좋아요 API는 POST 요청을 보냅니다.
    httpRequest('POST', url, null, success, fail);
}

// 로그아웃 기능
const logoutButton = document.getElementById('logout-btn');

if (logoutButton) {
    logoutButton.addEventListener('click', event => {
        function success() {
            // 로컬 스토리지에 저장된 액세스 토큰을 삭제
            localStorage.removeItem('access_token');

            // 쿠키에 저장된 리프레시 토큰을 삭제
            deleteCookie('refresh_token');
            location.replace('/login');
        }
        function fail() {
            alert('로그아웃 실패했습니다.');
        }

        httpRequest('DELETE','/api/refresh-token', null, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// 쿠키를 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    // 헤더 설정
    const headers = {
        Authorization: 'Bearer ' + localStorage.getItem('access_token'),
    };

    // FormData가 아닐 때만 application/json 헤더를 설정
    // FormData는 브라우저가 자동으로 boundary를 포함한 Content-Type을 설정함
    if (body && !(body instanceof FormData)) {
        headers['Content-Type'] = 'application/json';
    }

    fetch(url, {
        method: method,
        headers: headers,
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            // 좋아요 기능을 위해 서버가 반환한 값을 success 콜백으로 전달
            // 응답 본문이 있는 경우 JSON 파싱
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return response.json().then(data => success(data));
            }
            // 본문이 없거나 JSON이 아닌 경우
            return success(null);
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail(response.status));
        } else {
            return fail(response.status);
        }
    });
}