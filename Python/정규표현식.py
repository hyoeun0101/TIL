# # *  0개~      + 1개~       ? 있거나 없거나

# import re

# # match객체 또는 None 반환
# m = re.match('[a-z]+', '3 python')
# print(m)

# m = re.search('[a-z]+', '3 python')
# print(m)

# # 리스트 반환
# m = re.findall('[a-z]+', 'life is too short')
# print(m)

# # iterator 객체 반환
# m = re.finditer('[a-z]+', 'life is too short')
# print(m)
# for i in m:
#     print(i)

# #### 컴파일 옵션 ###

# # DOTALL : \n문자 포함하여 매치
# p = re.compile('a.b', re.DOTALL)
# m = p.match('a\nb')
# print(m)

# # MULTILINE : 멀티라인, 문자열 전체를 보는게 아니라 각 라인마다 매치.
# p = re.compile('^python\s\w+', re.MULTILINE)

# data = """python one
# life is too short
# python two
# you need python
# python three"""

# print(p.findall(data))

# # IGNORECASE : 대소문자 구별 없이 매치
# p = re.compile('[a-z]+', re.I)
# m = p.match('SddFfSDF')
# print(m)

# # VERBOSE : 정규식을 줄 단위로 구분하기

# charref = re.compile(r"""
# &[#]                # Start of a numeric entity reference
# (
#     0[0-7]+         # Octal form
#     | [0-9]+          # Decimal form
#     | x[0-9a-fA-F]+   # Hexadecimal form
# )
# ;                   # Trailing semicolon
# """, re.VERBOSE)

# #### raw string ###
# p = re.search('\\\\section', "\section")
# print('p: ', p)

# # | : 또는   ^ : 처음시작과 매치   $ : 끝과 매치
# # \A : ^와 비슷. MULTILINE 옵션 사용 시 라인과 상관없이 문자열 전체와 매치
# # \Z : $와 비슷. MULTILINE 옵션 사용 시 라인과 상관없이 문자열 전체와 매치
# # \b : Word Boundary. 단어 구분!! 파이썬 리터럴 규칙 중 \b는 백스페이스 의미하므로 r써줘야한다.
# p = re.search(r'\bclass\b', 'no class at all')
# print(p)
# # \B : \b와 반대.

# # 그루핑
# p = re.compile('(?P<name>\w+)\s\d+[-]\d+[-]\d+')

# m = p.search('kim 010-1234-5678')
# print(m.group('name'))

# # 그루핑 재참조
# p = re.compile(r'(\b\w+)\s+\1')
# m = p.search('paris in the the spring').group()
# print(m)

# # 긍정 전방 탐색 : 정규식과 매치되어도 문자가 나오지 않는다
# p = re.compile('.+(?=:)')
# m = p.search("http://google.com")
# print(m.group())

# # 부정 전방 탐색
# # ex) 파일 확장자 bat, exe인 파일은 제외

# # .*[.](?!bat$|exe$).*$

# # 문자열 바꾸기 sub
# p = re.compile('blue|white|red')
# m = p.sub('colour', 'blue socks and red shoes', count=1)
# print(m)

# p = re.compile(r'(?P<name>\w+)\s+(?P<phone>(\d+)[-]\d+[-]\d+)')
# m = p.sub("\g<phone> \g<name>", 'kim 010-1234-5678')
# print(m)


# # 숫자를 16진수로 바꾸기
# def hexrepl(match):
#     value = int(match.group())
#     return hex(value)


# p = re.compile(r'\d+')
# m = p.sub(hexrepl, 'Call 65490 fir printing, 6547 for user code.')
# print(m)

# # Greedy
# s = '<html><head><title>Title</title>'

# print(re.match('<.*?>', s).group())

import re
p = re.compile('[a-z]+', re.I)
m = p.match('pyTHON')
print(m)
