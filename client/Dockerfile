FROM node:latest

WORKDIR /app

COPY . .

RUN touch .env
COPY ./deployment/.env.stag .env

RUN npm install

CMD ["npm", "start"]