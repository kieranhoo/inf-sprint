FROM node:latest

WORKDIR /app

COPY . .

RUN touch .env
COPY ./deployments/.env.stag .env

RUN npm install

CMD ["npm", "start"]