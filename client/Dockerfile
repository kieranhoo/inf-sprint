FROM node:latest

WORKDIR /app

COPY . .

RUN cp ./deployments/.env.stag .env

RUN npm install

CMD ["npm", "start"]